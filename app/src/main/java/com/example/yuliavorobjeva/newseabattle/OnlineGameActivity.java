package com.example.yuliavorobjeva.newseabattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OnlineGameActivity extends AppCompatActivity implements OnBattleCellClickListener{

    private final static int INFO_DIALOG = 1;
    private final static int WINNER_FIRST_DIALOG = 2;
    private final static int WINNER_SECOND_DIALOG = 3;

    private RecyclerView mRecyclerForFirst;
    private RecyclerView mRecyclerForSecond;

    private TextView mFirstTV;
    private TextView mSecondTV;
    private Typeface mTypeface;


    private List<Cell> mCellsFirst;
    private List<Cell> mCellsSecond;
    private List<BattleCell> mBattleCellFirst;
    private List<BattleCell> mBattleCellSecond;

    private BattleFieldForOnlineAdapter mBattleFieldAdapterForFirst;
    private BattleFieldForTwoAdapter mBattleFieldAdapterForSecond;

    private BattleCellsController mBattleCellsControllerForFirst;
    private BattleCellsController mBattleCellsControllerForSecond;



    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private MyBroadCastReciever receiver;

    Intent intent;
    private List<Cell> mMyCells;
    private List<BattleCell> mMyBattleCells;
    private List<BattleCell> mOppositBattleCells;
    private List<Integer> mMyIntCells;
    private Boolean isWifiP2pEnabled;

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        /*
           UI PART


         */

        intent = getIntent();

        mFirstTV = (TextView) findViewById(R.id.first_tv);
        mSecondTV = (TextView) findViewById(R.id.second_tv);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        mFirstTV.setTypeface(mTypeface);
        mSecondTV.setTypeface(mTypeface);

        mBattleCellFirst = new ArrayList<>();
        mBattleCellSecond = new ArrayList<>();

        mCellsFirst = (List<Cell>) getIntent().getSerializableExtra("positions1");
        //mCellsSecond = (List<Cell>) getIntent().getSerializableExtra("positions2");


        for (int i = 0; i < mCellsFirst.size(); i++) {
            if (mCellsFirst.get(i).getState() == Cell.State.FILLED) {
                mBattleCellFirst.add(new BattleCell(i, BattleCell.State.SHIP));
            } else {
                mBattleCellFirst.add(new BattleCell(i, BattleCell.State.EMPTY));
            }
        }


        mBattleCellsControllerForFirst = new BattleCellsController(mBattleCellFirst);
        //mBattleCellsControllerForSecond = new BattleCellsController(mBattleCellSecond);


        mRecyclerForFirst = (RecyclerView) findViewById(R.id.recyclerViewForFirst);
        //mRecyclerForSecond = (RecyclerView) findViewById(R.id.recyclerViewForSecond);

        mBattleFieldAdapterForFirst = new BattleFieldForOnlineAdapter(this, mBattleCellsControllerForFirst.getCells(), this, 1);
        // mBattleFieldAdapterForSecond = new BattleFieldForTwoAdapter(this, mBattleCellsControllerForSecond.getCells(), this, 2);
        mRecyclerForFirst.setAdapter(mBattleFieldAdapterForFirst);
        // mRecyclerForSecond.setAdapter(mBattleFieldAdapterForSecond);

        mRecyclerForFirst.setLayoutManager(new GridLayoutManager(this, 10));
        //mRecyclerForSecond.setLayoutManager(new GridLayoutManager(this, 10));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerForFirst.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        //mRecyclerForSecond.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        //В САМОМ КОНЦЕ ПОЧЕМУ ТО
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
                Log.e("discoverPeers", "success");
            }

            @Override
            public void onFailure(int reasonCode) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
                Log.e("discoverPeers", "notSuccess");
            }
        });

        List peers = new ArrayList();
        WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {  //Out with the old, in with the new. peers.clear();
                // peers.addAll(peerList.getDeviceList());
                // If an AdapterView is backed by this data, notify it
                // of the change. For instance, if you have a ListView of available
                // peers, trigger an update.
                //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
                //if (peers.size() == 0) { Log.d(WiFiDirectActivity.TAG, "No devices found"); return;
                // }
            }

        };
    }

    @Override
    public void OnBattleCellClick(int position, int which) {
        if (which == 1) {
            mBattleCellsControllerForFirst.performClick(position);
            mBattleFieldAdapterForFirst.updateList(mBattleCellsControllerForFirst.getCells());
            if (isWinner(mBattleCellsControllerForFirst.getCells())) {
                Log.e("firts win", "first");
                showDialog(WINNER_FIRST_DIALOG);
            }

        } else if (which == 2) {
            mBattleCellsControllerForSecond.performClick(position);
            mBattleFieldAdapterForSecond.updateList(mBattleCellsControllerForSecond.getCells());
            if (isWinner(mBattleCellsControllerForSecond.getCells())) {
                Log.e("second win", "second");
                showDialog(WINNER_SECOND_DIALOG);

            }
        }
    }

    private boolean isWinner(List<BattleCell> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getState() == BattleCell.State.SHIP) {
                return false;
            }
        }
        return true;
    }

    protected Dialog onCreateDialog(int id) {
        if (id == INFO_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Please, read info");
            adb.setMessage("Purple color - you crashed a ship \n Grey color - you missed");
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.setNegativeButton("Ok", infoDialogListener);
            return adb.create();
        } else if( id == WINNER_FIRST_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("WE HAVE A WINNER!:)");
            adb.setMessage("Second player won! Congratulations!");
            adb.setIcon(R.drawable.salut);
            adb.setPositiveButton("Ok", winnerDialogListener);
            adb.setCancelable(false);
            return adb.create();
        } else if( id == WINNER_SECOND_DIALOG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("WE HAVE A WINNER!:)");
            adb.setMessage("First player won! Congratulations!");
            adb.setIcon(R.drawable.salut);
            adb.setPositiveButton("Ok", winnerDialogListener);
            adb.setCancelable(false);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener infoDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_NEGATIVE) {
            }
        }
    };

    DialogInterface.OnClickListener winnerDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(i == Dialog.BUTTON_POSITIVE) {
                Intent intent = new Intent(OnlineGameActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        receiver = new MyBroadCastReciever(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
        Log.e("melog", "register receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        Log.e("melog", "unregister receiver");
    }
}
