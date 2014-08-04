package com.intuit.libs.zookeeper.testcodes;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class GroupWatcher implements Watcher  {
	private ChildrenWatcher childrenWatcher;
	private String groupName;
	
	public GroupWatcher(String hosts, String group) throws IOException, InterruptedException {
		childrenWatcher = new ChildrenWatcher();
		childrenWatcher.connect(hosts);
		groupName = group;
	}
	
	public void list() throws KeeperException,
			InterruptedException {
		String path = "/" + groupName;
		StringBuilder sb = new StringBuilder(100);
		try {
			List<String> children = childrenWatcher.getChildren(path, this);
//			if (children.isEmpty()) {
//				System.out.printf("No members in group %s\n", groupName);
//			}
			sb.append(groupName);
			sb.append(":[");
			for (String child : children) {
				sb.append(child);
				sb.append(" ");
			}
			sb.append("]");
			System.out.printf("%s\n", sb.toString());
			
		} catch (KeeperException.NoNodeException e) {
			System.out.printf("Group %s does not exist\n", groupName);
			System.exit(1);
		}
	}
	
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeChildrenChanged) {
			try {
				list();
			} catch (InterruptedException e) {
				System.err.println("Interrupted. Exiting.");
				Thread.currentThread().interrupt();
			} catch (KeeperException e) {
				System.err.printf("KeeperException: %s. Exiting.\n", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		GroupWatcher groupWatcher = new GroupWatcher(args[0], args[1]);
		groupWatcher.list();
		Thread.sleep(Long.MAX_VALUE);
		
	}
}
