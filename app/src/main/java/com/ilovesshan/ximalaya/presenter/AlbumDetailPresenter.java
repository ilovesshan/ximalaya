package com.ilovesshan.ximalaya.presenter;

import com.ilovesshan.ximalaya.interfaces.IAlbumDetail;
import com.ilovesshan.ximalaya.interfaces.IAlbumDetailViewController;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: ilovesshan
 * @date: 2022/5/28
 * @description:
 */
public class AlbumDetailPresenter implements IAlbumDetail {
    private final List<IAlbumDetailViewController> mIAlbumDetailViewControllers = new ArrayList<>();
    private Album mAlbum;

    public static AlbumDetailPresenter sAlbumDetailPresenter = null;

    private AlbumDetailPresenter() {
    }

    public static AlbumDetailPresenter getInstance() {
        if (sAlbumDetailPresenter == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sAlbumDetailPresenter == null) {
                    sAlbumDetailPresenter = new AlbumDetailPresenter();
                }
            }
        }
        return sAlbumDetailPresenter;
    }


    @Override
    public void loadDetailListData() {

    }

    @Override
    public void registerViewController(IAlbumDetailViewController viewController) {
        if (!this.mIAlbumDetailViewControllers.contains(viewController)) {
            this.mIAlbumDetailViewControllers.add(viewController);
            if (mAlbum != null) {
                viewController.onLoadedDetail(mAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewController(IAlbumDetailViewController viewController) {
        this.mIAlbumDetailViewControllers.remove(viewController);
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }
}
