package com.intuit.libs.zookeeper.testcodes;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;

public class ChildrenWatcher extends ConnectionWatcher {
	public List<String> getChildren(String path, Watcher watcher)
			throws InterruptedException, KeeperException {
		List<String> children = zk.getChildren(path, watcher);
		return children;
	}
}