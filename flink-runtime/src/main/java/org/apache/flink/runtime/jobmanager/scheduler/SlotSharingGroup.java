/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.jobmanager.scheduler;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.flink.runtime.instance.SlotSharingGroupAssignment;
import org.apache.flink.runtime.instance.SlotSharingGroupId;
import org.apache.flink.runtime.jobgraph.JobVertexID;

/**
 * A slot sharing units defines which different task (from different job vertices) can be
 * deployed together within a slot. This is a soft permission, in contrast to the hard constraint
 * defined by a co-location hint.
 */
/**
 * 我们可以把不能被chain成一条的两个操作如flatmap和key&sink放在一个TaskSlot里执行，这样做可以获得以下好处
 * SlotSharingGroup是Flink中用来实现slot共享的类，它尽可能地让subtasks共享一个slot */
/** 共用slot使得我们不再需要计算每个任务需要的总task数目，直接取最高算子的并行度即可
 对计算资源的利用率更高。例如，通常的轻量级操作map和重量级操作Aggregate不再分别需要一个线程，而是可以在同一个线程内执行，而且对于slot有限的场景，我们可以增大每个task的并行度了。
 接下来我们还是用官网的图来说明flink是如何重用slot的： */
/** 更好的公用资源，有些操作任务不需要使用完整的一个Task的资源，所以在这个TaskSlot中，允许运行别的Task*/
public class SlotSharingGroup implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	

	private final Set<JobVertexID> ids = new TreeSet<JobVertexID>();
	
	/** Mapping of tasks to subslots. This field is only needed inside the JobManager, and is not RPCed. */
	private transient SlotSharingGroupAssignment taskAssignment;

	private final SlotSharingGroupId slotSharingGroupId = new SlotSharingGroupId();
	
	public SlotSharingGroup() {}
	
	public SlotSharingGroup(JobVertexID ... sharedVertices) {
		for (JobVertexID id : sharedVertices) {
			this.ids.add(id);
		}
	}

	// --------------------------------------------------------------------------------------------
	
	public void addVertexToGroup(JobVertexID id) {
		this.ids.add(id);
	}
	
	public void removeVertexFromGroup(JobVertexID id) {
		this.ids.remove(id);
	}
	
	public Set<JobVertexID> getJobVertexIds() {
		return Collections.unmodifiableSet(ids);
	}

	public SlotSharingGroupId getSlotSharingGroupId() {
		return slotSharingGroupId;
	}

	public SlotSharingGroupAssignment getTaskAssignment() {
		if (this.taskAssignment == null) {
			this.taskAssignment = new SlotSharingGroupAssignment();
		}
		
		return this.taskAssignment;
	}
	
	public void clearTaskAssignment() {
		if (this.taskAssignment != null) {
			if (this.taskAssignment.getNumberOfSlots() > 0) {
				throw new IllegalStateException("SlotSharingGroup cannot clear task assignment, group still has allocated resources.");
			}
		}
		this.taskAssignment = null;
	}
	
	// ------------------------------------------------------------------------
	//  Utilities
	// ------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SlotSharingGroup " + this.ids.toString();
	}
}
