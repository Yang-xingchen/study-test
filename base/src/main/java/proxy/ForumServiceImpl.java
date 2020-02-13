package proxy;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ForumServiceImpl implements ForumService {
	private static final Logger LOGGER = LogManager.getLogger(ForumServiceImpl.class);

	@Override
	public void removeTopic(int topicId) {
		LOGGER.info("removeTopic:"+topicId);
	}

	@Override
	public void removeForum(int forumId) {
		LOGGER.info("removeForum:"+forumId);
	}

    public String test(){
	    LOGGER.info("this is ForumServiceImpl");
	    return "this is ForumServiceImpl : "+this.toString();
    }

}
