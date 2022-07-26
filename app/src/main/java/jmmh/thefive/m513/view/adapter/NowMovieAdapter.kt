package jmmh.thefive.m513.view.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jmmh.thefive.m513.R
import jmmh.thefive.m513.data.repository.NetworkState
import jmmh.thefive.m513.model.NowMovie
import jmmh.thefive.m513.view.ui.fragment.DetailsFragment
import jmmh.thefive.m513.data.utils.Constants
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
class NowMovieAdapter(public val context: Context) : PagedListAdapter<NowMovie,
        RecyclerView.ViewHolder>(MovieDiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null


    private val items = ArrayList<NowMovie>()

    fun setItems(items: ArrayList<NowMovie>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<NowMovie>() {
        override fun areItemsTheSame(oldItem: NowMovie, newItem: NowMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NowMovie, newItem: NowMovie): Boolean {
            return oldItem == newItem
        }

    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: NowMovie?, context: Context) {

            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.nowvoteAverage
            val moviePosterURL = Constants.POSTER_BASE_URL + movie?.nowposterPath

            val data = ArrayList<String>()

            data.add(movie?.id.toString())
            data.add(movie?.nowposterPath.toString())
            data.add(movie?.nowreleaseDate.toString())
            data.add(movie?.nowvoteAverage.toString())
            data.add(movie?.title.toString())
            for (i in data.indices) {
                Log.e("Log_movies_item", "${data[i]} ")
            }
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster);
            itemView.setOnClickListener {
                val activity = it!!.context as AppCompatActivity
                val bundle = Bundle()

                movie?.id?.let { it1 -> bundle.putInt("movieId", it1) }
                val detailsFragment = DetailsFragment()
                detailsFragment.arguments = bundle
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(jmmh.thefive.m513.R.id.layout_Details, detailsFragment)
                    .addToBackStack(null).commit()
            }
        }

    }


    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            } else {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;

            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }

}