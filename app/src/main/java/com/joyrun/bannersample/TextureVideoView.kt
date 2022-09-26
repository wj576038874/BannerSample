package com.joyrun.bannersample

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.TextureView
import cn.jzvd.JZMediaInterface
import co.runner.app.widget.TextureVideoViewOutlineProvider
import java.io.IOException


/**
 * author: wenjie
 * date: 2021-05-12 16:13
 * descption:
 */
class TextureVideoView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    def: Int = 0
) : TextureView(context, attr, def), TextureView.SurfaceTextureListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mIsDataSourceSet = false
    private var mIsViewAvailable = false
    private var mIsVideoPrepared = false
    private var mIsPlayCalled = false
    private var mState = State.UNINITIALIZED
    private var SAVED_SURFACE: SurfaceTexture? = null

    init {
        initView()
    }

    private fun initView() {
        initPlayer()
        surfaceTextureListener = this
    }

    private fun initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        } else {
            mMediaPlayer?.reset()
        }
        mIsVideoPrepared = false
        mIsPlayCalled = false
        mState = State.UNINITIALIZED
    }

    /**
     * @see android.media.MediaPlayer.setDataSource
     */
    fun setDataSource(path: String?) {
        initPlayer()
        try {
            mMediaPlayer?.setDataSource(path)
            mIsDataSourceSet = true
            prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * @see android.media.MediaPlayer.setDataSource
     */
    fun setDataSource(context: Context, uri: Uri) {
        initPlayer()
        try {
            mMediaPlayer?.setDataSource(context, uri)
            mIsDataSourceSet = true
            prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * @see android.media.MediaPlayer.setDataSource
     */
    fun setDataSource(afd: AssetFileDescriptor) {
        initPlayer()
        try {
            val startOffset: Long = afd.startOffset
            val length: Long = afd.length
            mMediaPlayer?.setDataSource(afd.fileDescriptor, startOffset, length)
            mIsDataSourceSet = true
            prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun prepare() {
        try {
            mMediaPlayer?.setOnCompletionListener {
                mState = State.END
                mListener?.onVideoEnd()
            }

            mMediaPlayer?.prepareAsync()

            mMediaPlayer?.setOnPreparedListener {
                it.setVolume(0f, 0f)
                mIsVideoPrepared = true
                if (mIsPlayCalled && mIsViewAvailable) {
                    play()
                }
                mListener?.onVideoPrepared()
            }
            mMediaPlayer?.setOnErrorListener { mp, what, extra ->
                mListener?.onError(what)
                true
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            mListener?.onError(-1)
        } catch (e: SecurityException) {
            e.printStackTrace()
            mListener?.onError(-1)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            mListener?.onError(-1)
        }
    }

    fun play() {
        if (!mIsDataSourceSet) {
            return
        }
        mIsPlayCalled = true
        if (!mIsVideoPrepared) {
            return
        }
        if (!mIsViewAvailable) {
            return
        }
        if (mState === State.PLAY) {
            return
        }
        if (mState === State.PAUSE) {
            mState = State.PLAY
            mMediaPlayer?.start()
            return
        }
        if (mState === State.END || mState === State.STOP) {
            mState = State.PLAY
            mMediaPlayer?.seekTo(0)
            mMediaPlayer?.start()
            return
        }
        mState = State.PLAY
        mMediaPlayer?.start()
    }

    /**
     * Pause video. If video is already paused, stopped or ended nothing will happen.
     */
    fun pause() {
        if (mState === State.PAUSE) {
            return
        }
        if (mState === State.STOP) {
            return
        }
        if (mState === State.END) {
            return
        }
        mState = State.PAUSE
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.pause()
        }
    }

    /**
     * Stop video (pause and seek to beginning). If video is already stopped or ended nothing will
     * happen.
     */
    fun stop() {
        if (mState === State.STOP) {
            return
        }
        if (mState === State.END) {
            return
        }
        mState = State.STOP
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.pause()
            mMediaPlayer?.seekTo(0)
        }
    }

    /**
     * @see android.media.MediaPlayer.setLooping
     */
    fun setLooping(looping: Boolean) {
        mMediaPlayer?.isLooping = looping
    }

    fun setRadius(radius: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = TextureVideoViewOutlineProvider(radius)
            clipToOutline = true
        }
    }

    /**
     * @see android.media.MediaPlayer.seekTo
     */
    fun seekTo(milliseconds: Int) {
        mMediaPlayer?.seekTo(milliseconds)
    }

    /**
     * @see android.media.MediaPlayer.getDuration
     */
    fun getDuration(): Int {
        return mMediaPlayer?.duration ?: 0
    }

    private var mListener: MediaPlayerListener? = null

    /**
     * Listener trigger 'onVideoPrepared' and `onVideoEnd` events
     */
    fun setListener(listener: MediaPlayerListener?) {
        mListener = listener
    }

    abstract class MediaPlayerListener {
        open fun onVideoPrepared() {

        }

        open fun onVideoEnd() {

        }

        open fun onError(i: Int) {

        }
    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surfaceTexture
            val surface = Surface(SAVED_SURFACE)
            mMediaPlayer?.setSurface(surface)
            mIsViewAvailable = true
            if (mIsDataSourceSet && mIsPlayCalled && mIsVideoPrepared) {
                play()
                Log.e("asd", "play")
            }
            Log.e("asd", "null")
        } else {
            setSurfaceTexture(SAVED_SURFACE)
            Log.e("asd", "SAVED_SURFACE")
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {

    }

    enum class State {
        UNINITIALIZED, PLAY, STOP, PAUSE, END
    }

    companion object {
        var SAVED_SURFACE: Surface? = null
    }
}