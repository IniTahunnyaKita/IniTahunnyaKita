package com.molaja.android.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.molaja.android.R;
import com.molaja.android.adapter.ChatAdapter;
import com.molaja.android.adapter.ItemDetailPagerAdapter;
import com.molaja.android.fragment.itemdetail.OverviewFragment;
import com.molaja.android.model.ChatData;
import com.molaja.android.model.Discussion;
import com.molaja.android.model.HangoutPost;
import com.molaja.android.model.User;
import com.molaja.android.util.HardcodeValues;
import com.molaja.android.util.ImageUtil;
import com.molaja.android.widget.ViewPagerIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by tinklabs on 4/16/2015.
 */
public class ChatActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TextView.OnEditorActionListener, Target {
    public static String TAG = "ChatActivity";

    //Views
    SlidingUpPanelLayout slidingLayout;
    RecyclerView recyclerView;
    ViewPagerIndicator mViewPagerIndicator;
    ImageView profileBarBg;
    ImageButton emojiBtn, sendBtn;
    EditText chatInput;
    View profileBar;
    FloatingActionButton addImageBtn;

    ChatAdapter chatAdapter;

    Discussion.Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        conversation = (Discussion.Conversation) getIntent().getSerializableExtra("CONVERSATION");

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatInput.setOnEditorActionListener(this);
        //chatInput.addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatInput.setOnEditorActionListener(null);
        //chatInput.removeTextChangedListener(this);
    }

    private void setupViews() {
        //find views
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        recyclerView = (RecyclerView) findViewById(R.id.chat_content);
        profileBar = findViewById(R.id.profile_bar);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewpager_indicator);
        profileBarBg = (ImageView) findViewById(R.id.profile_bar_background);
        addImageBtn = (FloatingActionButton) findViewById(R.id.add_image_btn);
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);
        ImageView itemImage = (ImageView) findViewById(R.id.chat_item_image);
        TextView name = (TextView) findViewById(R.id.name);
        TextView itemName = (TextView) findViewById(R.id.item_name);
        chatInput = (EditText) findViewById(R.id.chat_input);
        sendBtn = (ImageButton) findViewById(R.id.send_button);

        initRecyclerView();

        Picasso.with(this)
                .load(getIntent().getStringExtra("PROFILE_PICTURE"))
                .fit()
                .into(profilePicture);

        Picasso.with(this)
                .load(getIntent().getStringExtra("PROFILE_PICTURE"))
                .into(this);

        Picasso.with(this)
                .load(conversation.image_url)
                .fit()
                .into(itemImage);

        name.setText(getIntent().getStringExtra("NAME"));
        itemName.setText(conversation.title);

        //ViewCompat.setElevation(addImageBtn, 20);
        addImageBtn.setRippleColor(getResources().getColor(R.color.DarkTeal));

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        ArrayList<Class<? extends Fragment>> pages = new ArrayList<>();

        int totalPage = HardcodeValues.ItemDetailValues.imageUrls.length;
        for(int i=0;i< totalPage;i++)
            pages.add(OverviewFragment.class);

        HangoutPost post = new HangoutPost();
        post.setItemUrl(conversation.image_url);
        post.setTitle(conversation.title);
        post.setPrice(210000);
        viewPager.setAdapter(new ItemDetailPagerAdapter(getSupportFragmentManager(), this, pages, post));
        viewPager.addOnPageChangeListener(this);

        slidingLayout.setDragView(profileBar);

        mViewPagerIndicator.initIndicators(HardcodeValues.ItemDetailValues.imageUrls.length);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(chatInput.getText().toString());
            }
        });

        //open chat layout after 1s delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                profileBar.performClick();
            }
        }, 1000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mViewPagerIndicator.selectIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        profileBarBg.setImageBitmap(ImageUtil.BlurBitmap(this, bitmap, 20));
        profileBarBg.setAlpha(0.5f);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    /**
     * Initialize the chat content.
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatAdapter = new ChatAdapter(new ArrayList<ChatData>());
        chatAdapter.setCurrentUser(User.getCurrentUser(this));

        recyclerView.setLayoutManager(layoutManager);
        FadeInAnimator animator = new FadeInAnimator();
        animator.setAddDuration(500);
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(chatAdapter);
    }

    /**
     * Send message to the conversation.
     * @param text The text to send
     */
    private void sendMessage(String text) {
        if (TextUtils.isEmpty(text))
            return;

        chatAdapter.addMessage(text);
        recyclerView.smoothScrollToPosition(0);
        chatInput.setText(null);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage(chatInput.getText().toString());
        }
        return true;
    }
}
