package com.homework.gardenapp;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantListFragment extends Fragment {

    private String etItem;

    public interface OnPlantSelectedListener {
        void onPlantSelected(int plantId);
    }

    private OnPlantSelectedListener mListener;

    public PlantListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etItem = getArguments().getString("e", "Watermelon");
    }

    public static PlantListFragment newInstance(String e) {
        PlantListFragment fragment = new PlantListFragment();
        Bundle args = new Bundle ();
        args.putString("e", e);
        fragment.setArguments(args);

        return fragment;
    }

    public void setSearch(String e){
        etItem = e;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate view
        View view = inflater.inflate(R.layout.fragment_plantlist, container, false);

        //Reference Recycler View
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.plant_list_fragment);

        //Set Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Populate Data into the adapter
        PlantAdapter adapter = new PlantAdapter(TrefleAPI.get(getContext(), etItem).getPlants());

        //Set adapter
        recyclerView.setAdapter(adapter);

        //Set animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private class PlantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Plant mPlant;
        private ImageView plantImage;
        private TextView mNameTextView;

        public PlantHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_plant, parent, false));
            itemView.setOnClickListener(this);
            plantImage = (ImageView) itemView.findViewById(R.id.plantImage);
            mNameTextView = (TextView) itemView.findViewById(R.id.plantName);
        }

        public void bind(Plant plant) {
            mPlant = plant;
            //Sets plant image using picasso
            Picasso.get().load(mPlant.getImageURL()).resize(250,250).into(plantImage);
            mNameTextView.setText(mPlant.getCommonName());
        }

        @Override
        public void onClick(View view) {
            // Tell Main Activity what plant was clicked
            mListener.onPlantSelected(mPlant.getId());
        }
    }

    private class PlantAdapter extends RecyclerView.Adapter<PlantHolder> {

        private List<Plant> mPlants;

        public PlantAdapter(List<Plant> plants) {
                mPlants = plants;
            }

            @Override
            public PlantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new PlantHolder(layoutInflater, parent);
            }

            @Override
            public void onBindViewHolder(PlantHolder holder, int position) {
                Plant plant = mPlants.get(position);
                holder.bind(plant);
            }

            @Override
            public int getItemCount() {
                return mPlants.size();
            }
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnPlantSelectedListener) {
                mListener = (OnPlantSelectedListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnPlantSelectedListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

}
