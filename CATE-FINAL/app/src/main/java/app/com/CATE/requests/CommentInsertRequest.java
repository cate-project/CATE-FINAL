package app.com.CATE.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CommentInsertRequest extends StringRequest {
    final static private String URL = "https://catapro.000webhostapp.com/fow_insertComment.php";
    private Map<String, String> parameters;

    public CommentInsertRequest(int videoID, int index, String author, String desc, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("videoID", videoID + "");
        parameters.put("index", index + "");
        parameters.put("author", author);
        parameters.put("desc", desc);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
