package edu.uw.ischool.hluo5.quizdroid

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopicAdapter(private val topics: List<Topic>, private val context: Context) :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)

        // 设置点击监听器，启动 TopicOverviewActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TopicOverviewActivity::class.java)
            intent.putExtra("topic", topic) // 传递整个 Topic 对象
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = topics.size

    class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val iconImageView: ImageView = itemView.findViewById(R.id.topic_icon)

        fun bind(topic: Topic) {
            titleTextView.text = topic.title
            descriptionTextView.text = topic.shortDescription
            iconImageView.setImageResource(topic.iconResId) // 设置图标
        }
    }
}
