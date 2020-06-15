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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {        

    public Collection <TimeRange> query(Collection <Event> events, MeetingRequest request) {
      long calculation = 0;
      long duration = request.getDuration();
      Collection<String> mandatoryAttendees = request.getAttendees();
      ArrayList<TimeRange> availableTimeForAll = new ArrayList<TimeRange>();
      int counter = 0;
      int end = TimeRange.END_OF_DAY;
      TimeRange meetingDuration;
      int meetingStart = 0;
      int meetingEnd = 0;
      int len = events.size();
      int start = TimeRange.START_OF_DAY;

      
      if(mandatoryAttendees.isEmpty()){
        availableTimeForAll.add(TimeRange.WHOLE_DAY);
        return availableTimeForAll;
      }
      if (duration > 60 * 24){
          return availableTimeForAll;
      }

      Set<String> attendeesSet = new HashSet<String>();
      for(String attendee : mandatoryAttendees) {
            attendeesSet.add(attendee);
      }
      int setSize = attendeesSet.size();

      if(len == 0) {
        availableTimeForAll.add(TimeRange.WHOLE_DAY);
        return availableTimeForAll;
      }

      for(Event event: events) {
        Collection<String> eventAttendees = event.getAttendees();
        TimeRange when = event.getWhen();        

        //if on first event
        if(counter == 0) {
            meetingStart = when.start();
            String attendee1 = attendeesSet.iterator().next();
            String eventAttendeePerson =  eventAttendees.iterator().next();

            //if attendee for event is not equal to meeting request attendee then make the availableTime all day
            if(eventAttendeePerson != attendee1){
                availableTimeForAll.add(TimeRange.WHOLE_DAY);
                return availableTimeForAll;
            }

            meetingEnd = when.end();
            availableTimeForAll.add(TimeRange.fromStartEnd(start, meetingStart,false));
            
            if(len == 1){
                availableTimeForAll.add(TimeRange.fromStartEnd(meetingEnd, end, true));
                return availableTimeForAll;
            }
        }
        //if second event
        if(counter == 1) {

            int newStart = meetingEnd;
            meetingStart = when.start();
            calculation = meetingStart - newStart;
            if(meetingEnd < meetingStart) {
                if(calculation < duration){
                    availableTimeForAll.clear();
                    return availableTimeForAll;
                }
                if(calculation == duration && setSize == 1){
                    int dur = (int)duration;
                    availableTimeForAll.clear();
                    availableTimeForAll.add(TimeRange.fromStartDuration(newStart, dur));
                    return availableTimeForAll;
                }
                else{
                    availableTimeForAll.add(TimeRange.fromStartEnd(newStart, meetingStart, false));
                    meetingEnd = when.end();
                    availableTimeForAll.add(TimeRange.fromStartEnd(meetingEnd, end, true));
                    return availableTimeForAll;
                }
            
            }
            meetingEnd = when.end();
            if(newStart > meetingEnd){
                availableTimeForAll.add(TimeRange.fromStartEnd(newStart, end, true));
                return availableTimeForAll;
            }
            else{
                meetingEnd = when.end();
                availableTimeForAll.add(TimeRange.fromStartEnd(meetingEnd, end, true));
                return availableTimeForAll;
            }
          }
          counter++;
        }
        //happens on last event
        //if(events[1])
        //int meetingEnd = when.getEnd();
        //availableTimeForAll.add(TimeRange.fromStartEnd(meetingEnd, end, true));

      return availableTimeForAll;
    }
}