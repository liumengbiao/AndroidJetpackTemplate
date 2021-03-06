package com.umairadil.androidjetpack.ui.movies.items

import android.animation.Animator
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import com.squareup.picasso.Picasso
import com.umairadil.androidjetpack.R
import com.umairadil.androidjetpack.models.movies.Movie
import com.umairadil.androidjetpack.utils.Constants
import com.umairadil.androidjetpack.utils.Utils
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.helpers.AnimatorHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.utils.DrawableUtils
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieItem(val movie: Movie) : AbstractFlexibleItem<MovieItem.ParentViewHolder>(),IFilterable<String> {

    init {
        isDraggable = false
        isSwipeable = true
        isSelectable = true
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement.
     */
    override fun equals(o: Any?): Boolean {
        if (o is MovieItem) {
            val inItem = o as MovieItem?
            return this.movie.id == inItem!!.movie.id
        }
        return false
    }

    override fun hashCode(): Int {
        return movie.id.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_movie
    }

    override fun filter(constraint: String?): Boolean {
        return movie.originalTitle != null && movie.originalTitle?.toLowerCase()?.trim()?.contains(constraint!!)!! ||
                movie.overview != null && movie.overview?.toLowerCase()?.trim()?.contains(constraint!!)!!
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ParentViewHolder {
        return ParentViewHolder(view!!, adapter!!)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: ParentViewHolder, position: Int, payloads: MutableList<Any>?) {

        holder.title.setText(movie.originalTitle)
        holder.overview.setText(movie.overview)
        holder.genre.setText(movie.genreNames)
        holder.year.setText(Utils.getInstance().formatDate(movie.releaseDate.toString()))
        holder.rating.rating = movie.voteAverage.toFloat()

        Picasso.with(holder.itemView.context).load(Constants.BASE_URL_IMAGE + movie.posterPath).into(holder.poster)

        val context = holder.itemView.context
        val drawable: Drawable = DrawableUtils.getSelectableBackgroundCompat(
                ContextCompat.getColor(context, R.color.colorWhite),
                ContextCompat.getColor(context, R.color.colorAccent), // pressed background
                ContextCompat.getColor(context, R.color.colorPrimaryDark)) // ripple color
        DrawableUtils.setBackgroundCompat(holder.frontView, drawable)
    }

    class ParentViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        val title: AppCompatTextView
        val overview: AppCompatTextView
        val genre: AppCompatTextView
        val year: AppCompatTextView
        val poster: AppCompatImageView
        val rating: RatingBar

        var fView: View
        var leftView: View
        var rightView: View
        var leftImage: ImageView
        var rightImage: ImageView

        var swiped = false

        init {
            this.title = view.txt_movie_title
            this.overview = view.txt_description
            this.genre = view.txt_popularity
            this.year = view.txt_year
            this.poster = view.img_movie_poster
            this.rating = view.rating_movie

            this.fView = view.front_view
            this.leftView = view.rear_left_view
            this.rightView = view.rear_right_view
            this.leftImage = view.left_image
            this.rightImage = view.right_image

            //Apply Fonts
            title.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)
            overview.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_REGULAR, itemView.context)
            genre.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)
            year.typeface = Utils.getInstance().setTypeface(Constants.FONT_ROBOTO_BOLD, itemView.context)

        }

        override fun scrollAnimators(animators: List<Animator>, position: Int, isForward: Boolean) {
            AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.recyclerView)
        }

        override fun onItemReleased(position: Int) {
            swiped = mActionState == ItemTouchHelper.ACTION_STATE_SWIPE
            super.onItemReleased(position)
        }

        override fun getActivationElevation(): Float {
            return 0f
        }

        override fun shouldActivateViewWhileSwiping(): Boolean {
            return true//default=false
        }

        override fun shouldAddSelectionInActionMode(): Boolean {
            return false//default=false
        }

        override fun getFrontView(): View {
            return fView
        }

        override fun getRearLeftView(): View {
            return leftView
        }

        override fun getRearRightView(): View {
            return rightView
        }
    }

    override fun toString(): String {
        return "MovieItem[" + super.toString() +  "]"
    }

}