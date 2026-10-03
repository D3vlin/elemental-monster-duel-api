package co.d3vlin.elementalmonsterduel.api.exception;

public class UnsupportedLanguageException extends RuntimeException {

    public UnsupportedLanguageException(String lang) {
        super("Unsupported lang: " + lang);
    }
}
