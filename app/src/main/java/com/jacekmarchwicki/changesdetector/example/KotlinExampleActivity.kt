package com.jacekmarchwicki.changesdetector.example

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appunite.universal_adapter_kotlin.*
import com.jacekmarchwicki.changesdetector.example.utils.ItemsGenerator
import com.jacekmarchwicki.universaladapter.BaseAdapterItem

class KotlinExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val adapter = UniversalAdapter(listOf(DataViewHolderManager()))

        // Alternatively you can use dynamic way of creating ViewHolderManager using factory class
        val adapterAlternative = UniversalAdapter(listOf(
                ViewHolderManagerFactory.create(R.layout.data_item) { DataViewHolderManager.DataViewHolder(it) }
        ))

        (findViewById<RecyclerView>(R.id.list_activity_recycler)).apply {
            layoutManager = LinearLayoutManager(
                    this@KotlinExampleActivity,
                    LinearLayoutManager.VERTICAL,
                    false).apply {
                recycleChildrenOnDetach = true
            }
            this.adapter = adapter
        }


        findViewById<View>(R.id.list_activity_fab).setOnClickListener {
            adapter.submitList(generateItems())
        }
    }

    private fun generateItems(): List<BaseAdapterItem> {
        return ItemsGenerator.generateElements()
                .map { DataItem(it.id, it.name, it.color) }
    }
}

class DataViewHolderManager : BaseViewHolderManager<DataItem>(
        R.layout.data_item, { DataViewHolder(it) }, DataItem::class
) {
    class DataViewHolder(view: View) : BaseViewHolder<DataItem>(view) {
        override fun bind(item: DataItem) {
            itemView.findViewById<TextView>(R.id.data_item_text).apply {
                text = item.name
                setOnClickListener { println("Click: $item") }
            }
            itemView.findViewById<CardView>(R.id.data_item_cardview)
                    .setCardBackgroundColor(item.color)
        }
    }
}

data class DataItem(val id: Long, val name: String, val color: Int) : DefaultAdapterItem<Long> {
    override val itemId: Long = id
}

// Another way to create ViewHolderManager with delegation
class DataViewHolderManagerAlternative : ViewHolderManager by ViewHolderManagerFactory.create(
        R.layout.data_item, { DataViewHolderManager.DataViewHolder(it) }
)