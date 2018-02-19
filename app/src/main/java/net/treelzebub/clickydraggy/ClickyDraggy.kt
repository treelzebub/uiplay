package net.treelzebub.clickydraggy

import android.os.Build
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup


class ClickyDraggy(
    private val destView: ViewGroup,
    private val listener: Listener
) : View.OnTouchListener, View.OnDragListener {

  interface Listener {
    fun viewSelectedPosition(position: Int)
  }

  private val viewsArrayList = mutableListOf<View>()

  init {
    destView.setOnDragListener(this)
  }

  fun addView(view: View) {
    viewsArrayList.add(view.apply {
      setOnTouchListener(this@ClickyDraggy)
    })
  }

  fun addViews(vararg views: View) {
    views.forEach { addView(it) }
  }

  override fun onDrag(layoutview: View, dragevent: DragEvent): Boolean {
    val action = dragevent.action
    val view = dragevent.localState as View

    if (action == DragEvent.ACTION_DROP) {
      val parent = view.parent as ViewGroup
      parent.removeView(view)

      val container = layoutview as ViewGroup
      container.addView(view.apply {
        visibility = View.VISIBLE
      })

      if (container.id == destView.id) {
        view.setOnTouchListener(null)
        parent.setOnDragListener(null)
      }

      viewsArrayList.find { view.id == it.id }?.let {
        listener.viewSelectedPosition(viewsArrayList.indexOf(it))
      }
    } else if (action == DragEvent.ACTION_DRAG_ENDED && dropEventNotHandled(dragevent)) {
      view.visibility = View.VISIBLE
    }
    return true
  }

  private fun dropEventNotHandled(dragEvent: DragEvent) = !dragEvent.result

  override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
    return if (motionEvent.action == MotionEvent.ACTION_DOWN) {
      val shadowBuilder = View.DragShadowBuilder(view)
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
        view.startDragAndDrop(null, shadowBuilder, view, 0)
      } else {
        view.startDrag(null, shadowBuilder, view, 0)
      }
      view.visibility = View.INVISIBLE
      true
    } else false
  }
}