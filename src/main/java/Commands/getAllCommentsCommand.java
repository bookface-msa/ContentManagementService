package Commands;

import java.util.HashMap;

public class getAllCommentsCommand extends CommentCommand{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        getService().getALLComments((String) map.get("postId"));
        return null;
    }
}
