package backend343.repository;

import backend343.chatRoom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    //query to check chatroom_users table
    @Query("SELECT COUNT(c) > 0 FROM ChatRoom c JOIN c.users u WHERE c.id = :chatroomId AND u.id = :userId")
    boolean isUserInChatroom(@Param("chatroomId") Long chatroomId, @Param("userId") Long userId);

}
