package Commands;

import com.bookface.postsservice.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentCommand extends Command{
    @Autowired
    private CommentsService service;

    public CommentsService getService() {
        return service;
    }

    @Autowired
    public final void setService(CommentsService service) {
        this.service = service;
    }
}
