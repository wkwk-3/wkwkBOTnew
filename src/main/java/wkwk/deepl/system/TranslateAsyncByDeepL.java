package wkwk.deepl.system;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import wkwk.deepl.record.TranslateRecord;

public class TranslateAsyncByDeepL {
    public TranslateRecord byDeepLToAPI(TranslateRecord translateRecord) {
        String authKey = "da11b0cd-9bd0-6dbe-a6f5-f99fe29177ca:fx";  // Replace with your key
        Translator translator = new Translator(authKey);
        TextResult result;
        try {
            result = translator.translateText(translateRecord.getText(), null, translateRecord.getLanguage());
            translateRecord.setResult(result.getText());
            translateRecord.setComplete(true);
        } catch (DeepLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return translateRecord;
    }
}
