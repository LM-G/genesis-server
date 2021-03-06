package com.solofeed.genesis.core.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.solofeed.genesis.core.exception.MarshallerError;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link MessageBodyConverter}
 */
@Ignore // TODO à déplacer dans JSONMapper
@RunWith(MockitoJUnitRunner.class)
public class MessageBodyConverterTest {
    private MessageBodyConverter<TestObject> messageBodyConverter;

    @Mock
    private JSONConverter jsonConverter;

    @Before
    public void setUp() {
        messageBodyConverter = new MessageBodyConverter<>(jsonConverter);
    }

    @Test
    public void shouldFindTestObjectWritableAndReadable() {
        // excecution
        boolean writable = messageBodyConverter.isWriteable(TestObject.class, null, null, null);
        boolean readable = messageBodyConverter.isReadable(TestObject.class, null, null, null);

        // assertions
        assertThat(writable).isTrue();
        assertThat(readable).isTrue();
    }

    @Test
    public void shouldWritesTestObject() throws IOException, URISyntaxException {
        // init test inputs
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        TestObject testObject = initTestObject();

        // excecution
        messageBodyConverter.writeTo(testObject, TestObject.class, null, null, null, null, stream);

        // assertions
        TestObject actualTestObject = new Gson().fromJson(convertToReader(stream), TestObject.class);

        assertThat(actualTestObject).isEqualToComparingFieldByFieldRecursively(testObject);
    }

    @Test
    public void shouldReadTestObject() throws IOException, URISyntaxException {
        // init test inputs
        TestObject testObject = initTestObject();
        String jsonifiedTestObject = new Gson().toJson(testObject);

        // stubbing
        ByteArrayInputStream inputStream = spy(new ByteArrayInputStream(jsonifiedTestObject.getBytes(StandardCharsets.UTF_8)));

        // excecution
        messageBodyConverter.readFrom(TestObject.class, null, null, null, null, inputStream);

        // assertions
        verify(inputStream).close();
    }

    @Test
    public void shouldThrowMarshallingException() throws IOException {
        // init test inputs
        String jsonParseErrorMsg = "foo";
        TestObject testObject = initTestObject();

        // stubbing
        Gson mockedGson = mock(Gson.class);
        when(mockedGson.toJson(any(TestObject.class))).thenThrow(new JsonParseException(jsonParseErrorMsg));
        // set the mocked Gson instance
        ReflectionTestUtils.setField(messageBodyConverter, "gson", mockedGson);

        // excecution
        assertThatThrownBy(() -> messageBodyConverter.writeTo(testObject, TestObject.class, null, null, null, null, new ByteArrayOutputStream()))
            // assertions
            .isInstanceOf(TechnicalException.class)
            .hasCauseExactlyInstanceOf(JsonParseException.class)
            .hasMessage("Erreur lors de la sérialisation")
            .extracting(e -> TechnicalException.class.cast(e).getCode())
            .containsExactly(MarshallerError.E_COULDNOT_MARSHAL);
    }

    @Test
    public void shouldThrowUnmarshallingException() throws IOException {
        // init test inputs
        String jsonParseErrorMsg = "foo";
        TestObject testObject = initTestObject();
        String jsonifiedTestObject = new Gson().toJson(testObject);

        // stubbing
        Gson mockedGson = mock(Gson.class);
        ByteArrayInputStream inputStream = spy(new ByteArrayInputStream(jsonifiedTestObject.getBytes(StandardCharsets.UTF_8)));
        when(mockedGson.fromJson(any(InputStreamReader.class), eq(TestObject.class))).thenThrow(new JsonParseException(jsonParseErrorMsg));

        // set the mocked Gson instance
        ReflectionTestUtils.setField(messageBodyConverter, "gson", mockedGson);

        // excecution
        assertThatThrownBy(() -> messageBodyConverter.readFrom(TestObject.class, null, null, null, null, inputStream))
            // assertions
            .isInstanceOf(TechnicalException.class)
            .hasCauseExactlyInstanceOf(JsonParseException.class)
            .hasMessage("Erreur lors de la désérialisation")
            .extracting(e -> TechnicalException.class.cast(e).getCode())
            .containsExactly(MarshallerError.E_COULDNOT_UNMARSHAL);
    }

    /**
     * Converts a stream of bytes to a buffered reader
     */
    private BufferedReader convertToReader(ByteArrayOutputStream stream) {
        InputStream input = new ByteArrayInputStream(stream.toByteArray());
        return new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Initialialize a test object
     *
     * @return object to test
     */
    private TestObject initTestObject() {
        return TestObject.builder()
            .prop_primitive("foo")
            .prop_object(TestObject.builder().prop_primitive("bar").build())
            .prop_collection(Collections.singletonList(TestObject.builder().prop_primitive("foobar").build()))
            .build();
    }

    /**
     * Test object to be serialized/deserialized containing an array of objets, a primitive and an object
     */
    @Builder
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TestObject {
        private String prop_primitive;
        private Collection<TestObject> prop_collection;
        private TestObject prop_object;
    }
}
