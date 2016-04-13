package ro.incremental.anaf.declaratii;

import com.google.common.base.MoreObjects;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 14/04/16.
 */
public class Result {
    public String message;
    public String fileId;
    public String decName;
    public File pdfFile;
    public int resultCode;

    public Result(String message, String fileId, int resultCode) {
        this.message = message;
        this.fileId = fileId;
        this.resultCode = resultCode;
    }

    public Result(String message, String fileId, int resultCode, File pdfFile, String decName) {
        this.message = message;
        this.fileId = fileId;
        this.resultCode = resultCode;
        this.pdfFile = pdfFile;
        this.decName = decName;
    }

    public String toJSON() {
        JSONObject ret = new JSONObject();

        ret.put("message", this.message);
        ret.put("fileId", this.fileId);
        ret.put("decName", this.decName);
        ret.put("resultCode", this.resultCode);

        return ret.toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Result")
                .add("Message", message)
                .add("FileId", fileId)
                .add("DecName", decName)
                .add("ResultCode", resultCode)
                .toString();
    }
}
