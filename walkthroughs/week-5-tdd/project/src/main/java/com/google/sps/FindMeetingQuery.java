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
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    //meeting options that fit just the mandatory attendees
    ArrayList<TimeRange> meetingOptions = new ArrayList<>();

    //no options for too long of a request
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
        return meetingOptions;
    }

    //anytime works if no events or no attendees in the request
    Collection<String> requestAttendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    if (requestAttendees.isEmpty() && optionalAttendees.isEmpty() || events.isEmpty()){
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    //collect start and end times of events from mandatory and optional attendees
    ArrayList<Integer> startTimes = new ArrayList<>();
    ArrayList<Integer> endTimes = new ArrayList<>();
    ArrayList<Integer> startTimesOptional = new ArrayList<>();
    ArrayList<Integer> endTimesOptional = new ArrayList<>();

    Collection<String> eventAttendees = new ArrayList<>();
    Boolean haveMandatoryAttendee = false;

    for (Event event : events){
        Collection<String> currentEventAttendees = event.getAttendees();
        eventAttendees.addAll(event.getAttendees());
        for (String attendee : currentEventAttendees){
            TimeRange timeRange = event.getWhen();
            int start = timeRange.start();
            int end = timeRange.end();
            if(optionalAttendees.contains(attendee)){
                startTimesOptional.add(start);
                endTimesOptional.add(end);
            }
            else{
                haveMandatoryAttendee = true;
                startTimes.add(start);
                endTimes.add(end);
                break;
            }
        }
    }
    
    //anytime works if event attendees and request attendees are non-overlapping
    Set<String> attendeesCheckSet = new HashSet<String>();
    attendeesCheckSet.addAll(requestAttendees);
    attendeesCheckSet.addAll(optionalAttendees);
    attendeesCheckSet.addAll(eventAttendees);
    if (attendeesCheckSet.size() == requestAttendees.size()
        +optionalAttendees.size()+eventAttendees.size()) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    //sort all time clips of events of mandatory attendees
    ArrayList<Integer> allTimes = new ArrayList<>();
    allTimes.addAll(startTimes);
    allTimes.addAll(endTimes);
    Collections.sort(allTimes);

    //sort all time clips of events of optional attendees
    ArrayList<Integer> allTimesOptional = new ArrayList<>();
    allTimesOptional.addAll(startTimesOptional);
    allTimesOptional.addAll(endTimesOptional);
    Collections.sort(allTimesOptional);
    
    //for mandatory attendees
    //merge all overlapping events
    ArrayList<Integer> newTimeline = timeline(request, allTimes, startTimes, endTimes);
    if(haveMandatoryAttendee){
        getMeetingOptions(newTimeline, meetingOptions);
    }

    if(optionalAttendees.isEmpty()){
        return meetingOptions;
    }

    //include optional attendees and see
    //if one or more time slots exists so that both mandatory and optional attendees can attend, return those time slots
    //otherwise, return the time slots that fit just the mandatory attendees
    ArrayList<TimeRange> meetingOptionsForAll = new ArrayList<>();
 
    allTimesOptional.addAll(newTimeline);
    startTimesOptional.addAll(startTimes);
    endTimesOptional.addAll(endTimes);
    Collections.sort(allTimesOptional);
    ArrayList<Integer> newTimelineForAll = timeline(request, allTimesOptional, startTimesOptional, endTimesOptional);
    getMeetingOptions(newTimelineForAll, meetingOptionsForAll);

    if (!meetingOptionsForAll.isEmpty() || !haveMandatoryAttendee){
        return meetingOptionsForAll;
    }

    return meetingOptions;
  }


  public ArrayList<Integer> timeline(MeetingRequest request, ArrayList<Integer> allTimes, ArrayList<Integer> startTimes, ArrayList<Integer> endTimes) {

    ArrayList<Integer> newTimeline = new ArrayList<>();
    long requestDuration = request.getDuration();
    int lastEndTime = 0;

    Stack<Integer> startTimesToMatch = new Stack<>();
    for (int time : allTimes){
        if (startTimes.contains(time) && endTimes.contains(time)){
            continue;
        }
        if (startTimes.contains(time)){
            startTimesToMatch.push(time);
        }
        else{
            int currentStartTime = startTimesToMatch.peek();
            startTimesToMatch.pop();
            //if reach the bottom - matched pair
            if (startTimesToMatch.isEmpty()){
                //only add options long enough to satisfy the request
                long duration = currentStartTime - lastEndTime;
                if (duration >= requestDuration ||
                    duration < requestDuration && newTimeline.isEmpty()){
                    newTimeline.add(currentStartTime);
                    newTimeline.add(time);
                }
                else{
                    newTimeline.set(newTimeline.size()-1, time);
                }
                lastEndTime = time;
            }
        }
    }

    return newTimeline;
   }

   public void getMeetingOptions(ArrayList<Integer> timeline, ArrayList<TimeRange> meetingOptions){
        if (timeline.get(0) > TimeRange.START_OF_DAY){
            meetingOptions.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, timeline.get(0), false));
        }

        for (int i = 1; i < timeline.size()-2; i+=2) {
            meetingOptions.add(TimeRange.fromStartEnd(timeline.get(i), timeline.get(i+1), false));
        }
        
        if (timeline.get(timeline.size()-1) < TimeRange.END_OF_DAY){
            meetingOptions.add(TimeRange.fromStartEnd(timeline.get(timeline.size()-1), TimeRange.END_OF_DAY, true));
        }
   }        
}
    