package backend343.models;

import backend343.chatRoom.ChatRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    @JsonIgnore
    private Schedule schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "id")
    @JsonIgnore
    private ChatRoom chatroom;

    @ManyToOne
    @JoinColumn(name = "speaker_id")
    @JsonIgnore
    private Speaker speaker;

    public String getScheduleDate() {
        return schedule.getDate().toString();
    }

    public Long getChatroomId() {
        return chatroom.getId();
    }

    public String getSpeakerUsername() {
        return speaker != null ? speaker.getUsername() : "No speaker assigned yet";
    }

}





