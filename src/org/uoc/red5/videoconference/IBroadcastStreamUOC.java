package org.uoc.red5.videoconference;

import java.io.IOException;

import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.ResourceExistException;
import org.red5.server.api.stream.ResourceNotFoundException;
public interface IBroadcastStreamUOC extends IBroadcastStream {

	void saveAsUOC(String filePath, User user, Meeting meeting, boolean isAppend)
    throws IOException, ResourceNotFoundException, ResourceExistException;
	
}
