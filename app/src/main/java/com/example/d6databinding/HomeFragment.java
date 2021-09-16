package com.example.d6databinding;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

import com.example.d6databinding.databinding.FragmentHomeBinding;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {


    private final StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
    private FragmentHomeBinding binding;
    private View rootView;
    private int count;
    private ProgressBar progressBar;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        View rootView = binding.getRoot();
        //here data must be an instance of the class MarsDataProvider
        progressBar = rootView.findViewById(R.id.progress_storage);
        progressBar.setVisibility(View.VISIBLE);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setTotalImage(findTotalImages());
        binding.setTotalVideo(findTotalVideos());
        binding.setTotalMusic(findTotalMusics());
        String freeSpace = convertBytes(getInternalFreeSpace());
        binding.setFreeSpace("Còn Trống :" + freeSpace);
        String usedSpace = convertBytes(getInternalUsedSpace());
        binding.setUseSpace("Đã Dùng :" + usedSpace);
        String totalSpace = convertBytes(getInternalTotalSpace());
        binding.setTotalSpace("Tổng :" + totalSpace);
        progressBar.setVisibility(View.GONE);
    }

    private String findTotalImages() {
        count = 0;
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        if (cursor != null) {
            count = cursor.getCount();
        }
        cursor.close();
        return String.valueOf(count);
    }

    private String findTotalVideos() {
        count = 0;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return String.valueOf(count);
    }

    private String findTotalMusics() {
        count = 0;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AlbumColumns.ALBUM};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return String.valueOf(count);
    }


    public long getInternalFreeSpace() {
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        return bytesAvailable;
    }

    public long getInternalTotalSpace() {
        long bytesTotal = (stat.getBlockSizeLong() * stat.getBlockCountLong());
        return bytesTotal;
    }

    public long getInternalUsedSpace() {
        long bytesUsed = getInternalTotalSpace() - getInternalFreeSpace();
        return bytesUsed;
    }

    public static String convertBytes(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " PB";
        if (size >= Eb) return floatForm((double) size / Eb) + " EB";

        return "anything...";
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }


}