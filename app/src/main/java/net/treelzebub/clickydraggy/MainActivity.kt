package net.treelzebub.clickydraggy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ClickyDraggy.Listener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ClickyDraggy(dest_parent, this).apply {
      addViews(one_box, two_box, three_box)
    }
  }

  override fun viewSelectedPosition(position: Int) {
    Toast.makeText(this, "dragged view position = " + position, Toast.LENGTH_SHORT).show()
  }
}
