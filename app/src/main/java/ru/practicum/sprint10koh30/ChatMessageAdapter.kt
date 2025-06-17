package ru.practicum.sprint10koh30

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.sprint10koh30.ChatMessage.Companion.CURRENT_USER
import ru.practicum.sprint10koh30.ChatMessage.Companion.OTHER_USER
import java.text.SimpleDateFormat
import java.util.Locale

class ChatMessageAdapter(
    private val chatMessageList: List<ChatMessage>
) : RecyclerView.Adapter<ChatMessageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatMessageViewHolder {
        return when(viewType) {
            CURRENT_USER_VIEW_TYPE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_current_user_chat_message, parent, false)
                 CurrentUserChatMessageViewHolder(itemView)
            }
            OTHER_USER_VIEW_TYPE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_other_user_chat_message, parent, false)
                 OtherUserChatMessageViewHolder(itemView)
            }

            else -> {
                throw IllegalArgumentException("Unsupported view type")
            }
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.onBind(chatMessageList[position])
    }

    override fun getItemCount() = chatMessageList.size

    override fun getItemViewType(position: Int) = when(chatMessageList[position].authorId) {
        CURRENT_USER -> CURRENT_USER_VIEW_TYPE
        OTHER_USER  -> OTHER_USER_VIEW_TYPE
        else -> {
            throw IllegalArgumentException("Unsupported user Id")
        }
    }

    companion object {
        private const val CURRENT_USER_VIEW_TYPE = 1
        private const val OTHER_USER_VIEW_TYPE = 2
    }

}

abstract class ChatMessageViewHolder(open val itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(chatMessage: ChatMessage)

}

class CurrentUserChatMessageViewHolder(override val itemView: View) : ChatMessageViewHolder(itemView) {

    private val dateFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())

    override fun onBind(chatMessage: ChatMessage) {

        itemView.findViewById<TextView>(R.id.tv_message).text = chatMessage.messageText
        itemView.findViewById<TextView>(R.id.tv_message_date).text = dateFormatter.format(chatMessage.messageDate)
        itemView.findViewById<ImageView>(R.id.iv_status).setImageResource(
            when(chatMessage.status) {
                ChatMessage.ChatMessageStatus.Sent -> R.drawable.ic_sent
                ChatMessage.ChatMessageStatus.Delivered -> R.drawable.ic_delivered
                ChatMessage.ChatMessageStatus.Read -> R.drawable.ic_read
            }
        )


    }

}

class OtherUserChatMessageViewHolder(override val itemView: View) : ChatMessageViewHolder(itemView) {

    private val dateFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())

    override fun onBind(chatMessage: ChatMessage) {

        itemView.findViewById<TextView>(R.id.tv_message).text = chatMessage.messageText
        itemView.findViewById<TextView>(R.id.tv_message_date).text = dateFormatter.format(chatMessage.messageDate)

    }

}