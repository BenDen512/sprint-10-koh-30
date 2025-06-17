package ru.practicum.sprint10koh30

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.sprint10koh30.ChatMessage.ChatMessageStatus
import ru.practicum.sprint10koh30.ChatMessage.Companion.CURRENT_USER
import ru.practicum.sprint10koh30.ChatMessage.Companion.OTHER_USER
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val messageList : MutableList<ChatMessage> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = if(ime.bottom > 0) {
                ime.bottom
            } else {
                systemBars.bottom
            }
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                bottomPadding
            )
            insets
        }

        val etMessage = findViewById<EditText>(R.id.et_message)
        val ivAction = findViewById<ImageView>(R.id.iv_action)
        etMessage.doAfterTextChanged {
            it?.let {
                if(it.isNotBlank()) {
                    ivAction.setImageResource(R.drawable.ic_send)
                } else {
                    ivAction.setImageResource(R.drawable.ic_audio)
                }
            }
        }

        val rvItems = findViewById<RecyclerView>(R.id.rv_items)
        rvItems.adapter = ChatMessageAdapter(messageList)

        ivAction.setOnClickListener {
            val messageText = etMessage.text
            if(messageText.trim().isNotEmpty()) {
                messageList.add(
                    ChatMessage(
                        messageText = messageText.toString(),
                        messageDate = Date(),
                        status = ChatMessageStatus.Sent,
                        authorId = CURRENT_USER
                    )
                )

                rvItems.adapter?.notifyDataSetChanged()

                rvItems.postDelayed({
                    messageList.add(
                        ChatMessage(
                            messageText = "From GPT : " + messageText.toString(),
                            messageDate = Date(),
                            status = ChatMessageStatus.Sent,
                            authorId = OTHER_USER
                        )
                    )
                    rvItems.adapter?.notifyDataSetChanged()
                },
                    1000L

                )
            }
        }


    }
}

data class ChatMessage(
    val messageText:String,
    val messageDate: Date,
    val status: ChatMessageStatus,
    val authorId: Int,
) {
    enum class ChatMessageStatus {
        Sent, Delivered, Read
    }

    companion object {
        const val CURRENT_USER = 1
        const val OTHER_USER = 2
    }
}

