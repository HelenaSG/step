// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<TimeRange> meetingOptions = new ArrayList<>();

    //no options for too long of a request
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
        return meetingOptions;
    }

    Collection<String> attendees = request.getAttendees();
    if (attendees.isEmpty()||events.isEmpty()) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    ArrayList<Integer> startTime = new ArrayList<>();
    ArrayList<Integer> endTime = new ArrayList<>();

    for (Event event : events){
        TimeRange timeRange = event.getWhen();
        int start = timeRange.start();
        int end = timeRange.end();
        startTime.add(start);
        endTime.add(end);
    }
    
    ArrayList<Integer> allTime = new ArrayList<>();
    allTime.addAll(startTime);
    allTime.addAll(endTime);
    Collections.sort(allTime);
    
    //merge all overlapping events
    ArrayList<Integer> newTimeline = new ArrayList<>();
    Stack<Integer> startTimeToMatch = new Stack<>();
    for (int time : allTime){
        if (startTime.contains(time)){
            startTimeToMatch.push(time);
        }
        else{
            int currentStartTime = startTimeToMatch.peek();
            startTimeToMatch.pop();
            //if reach the bottom - matched pair
            if (startTimeToMatch.isEmpty()){
                newTimeline.add(currentStartTime);
                newTimeline.add(time);
            }
        }
    }
    
    if (newTimeline.get(0) > TimeRange.START_OF_DAY){
        meetingOptions.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, newTimeline.get(0), false));
    }

    for (int i = 1; i < newTimeline.size()-2; i += 2) {
        meetingOptions.add(TimeRange.fromStartEnd(newTimeline.get(i), newTimeline.get(i+1), false));
    }
    
    if (newTimeline.get(newTimeline.size()-1) < TimeRange.END_OF_DAY){
        meetingOptions.add(TimeRange.fromStartEnd(newTimeline.get(newTimeline.size()-1), TimeRange.END_OF_DAY, true));
    }

    return meetingOptions;
  }
}
    