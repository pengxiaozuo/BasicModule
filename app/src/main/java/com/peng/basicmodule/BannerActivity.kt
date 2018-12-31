package com.peng.basicmodule

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.peng.basic.common.BaseActivity
import com.peng.basic.util.LogUtils
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BaseActivity() {
    override fun configUi(config: UiConfig) {
        config.layoutId = R.layout.activity_banner
    }

    var play: MediaPlayer? = null
    var prepareInit = false

    override fun initView(contentView: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        val data = listOf(
            "http://img3.3lian.com/2013/c3/62/d/48.jpg",
            "http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg",
            "http://up.enterdesk.com/edpic_source/60/e8/be/60e8bee2be7ee1cb65c44d68bcb693e0.jpg"
        )
        val adapter = object : BannerView.Adapter() {

            override fun onCreateView(parent: BannerView, any: Any): View {
                if (data.indexOf(any) <= 1) {
                    return View.inflate(this@BannerActivity, R.layout.item_banner_image, null)
                } else if (data.indexOf(any) == 2) {
                    return View.inflate(this@BannerActivity, R.layout.item_banner_text, null)
                } else {
                    play = MediaPlayer.create(
                        this@BannerActivity,
                        Uri.parse("http://edge.ivideo.sina.com.cn/196362722.mp4?KID=sina,viask&Expires=1546358400&ssig=HVEQaYrf00")
                    )
                    prepareInit = true
                    val view = View.inflate(this@BannerActivity, R.layout.item_banner_video, null)
                    if (view is SurfaceView)
                        view.holder.addCallback(holderCallback)
                    return view
                }
//                val view = ImageView(this@BannerActivity)
//                view.scaleType = ImageView.ScaleType.CENTER_CROP
//                return view
            }

            override fun onBindView(view: View, position: Int) {
                val any = data[position]

                if (position != 3) {
                    if (play?.isPlaying == true) {

                        play?.stop()
                        prepareInit = false
                    }
                }
                if (view is ImageView)
                    Picasso.get().load(any as String).into(view as ImageView)
                else if (view is TextView)
                    (view as TextView).setText(any.toString())
                else if (view is SurfaceView) {
                    if (play?.isPlaying == false)
                        if (!prepareInit)
                            play?.prepare()
                    play?.start()
                }
            }
        }

        adapter.data = data

        banner.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }


    val holderCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            play!!.setDisplay(holder)
        }

    }

    override fun onDestroy() {
        play?.release()
        super.onDestroy()
    }
}