package Commands;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.model.Comment;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class createCommentCommand extends CommentCommand{
    @Override
    public Object execute(HashMap<String, Object> map) throws Exception {
        getService().createComment((CommentRequest) map.get("commentRequest"),(String)map.get("postId"));
        return "new comment created";

    }
}
