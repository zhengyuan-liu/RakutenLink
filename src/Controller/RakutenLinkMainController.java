package Controller;

import Model.RakutenLinkModel;
import View.RakutenLinkMainView;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

/**
 * Created by ChenLetian on 4/1/16.
 * RakutenLink的主Controller类
 */
public class RakutenLinkMainController extends AbstractController implements RakutenLinkViewDelegate, RakutenLinkBlockDataSource {

    private RakutenLinkMainView mainView;
    private RakutenLinkModel mainModel;

    public static final String GameHasWon = "GameWin";
    public static final String GameHasNoBlocksToClear = "GameHasNoBlocksToClear";
    public static final String GameTimesUp = "GameTimesUp";

    //*
    final int blockTypes = 20;
    final int rowNumber = 10;
    final int columnNumber = 20;
    /*/
    final int blockTypes = 3;
    final int rowNumber = 5;
    final int columnNumber = 6;
    //*/

    public RakutenLinkMainController() {
        mainModel = new RakutenLinkModel(rowNumber, columnNumber);
        mainModel.reset(blockTypes);
        addModel(mainModel);
        mainView = new RakutenLinkMainView(this, this);
        mainView.initializeRakutenLinkMainView(rowNumber, columnNumber);
        addView(mainView);
        resetSelectStatus();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (Objects.equals(evt.getPropertyName(), GameHasWon)) {
            mainView.didSuccess();
        }
        else
        if (Objects.equals(evt.getPropertyName(), GameHasNoBlocksToClear)) {
            mainView.reset();
        }
        else
        if (Objects.equals(evt.getPropertyName(), GameTimesUp)) {
            mainView.noTimeRemaining();
        }
    }

    public void shutDown(){
        // 以下为样例代码,仅仅体现了退出的逻辑 by Archimekai
        System.exit(0);
    }

    @Override
    public void shuffle(){
        mainModel.shuffle();
        mainView.reset();
        resetSelectStatus();
    }

    @Override
    public void restartGame() {
        mainModel.reset(blockTypes);
        mainView.reset();
        resetSelectStatus();
    }

    private int hasSelectedCount;
    private int hasSelectedRow;
    private int hasSelectedColumn;

    private void resetSelectStatus() {
        hasSelectedCount = 0;
        hasSelectedRow = -1;
        hasSelectedColumn = -1;
    }

    @Override
    public void DidClickBlockAtRowAndColumn(int row, int column) {
        hasSelectedCount++;
        if (hasSelectedCount == 1) {
            hasSelectedRow = row;
            hasSelectedColumn = column;
        }
        else {
            int[][] linkNodes=mainModel.getLinkNodes(hasSelectedRow, hasSelectedColumn, row, column);
            if(linkNodes.length!=0){
            //if (mainModel.Removable(hasSelectedRow, hasSelectedColumn, row, column)) {
                mainModel.clearTwoBlocks(hasSelectedRow, hasSelectedColumn, row, column);
                if ((hasSelectedRow != -1)) {
                    mainView.didClearTwoBlocksSuccessful(hasSelectedRow, hasSelectedColumn, row, column);
                }

                //TODO:draw line
                resetSelectStatus();
            }
            else {
                mainView.didClearTwoBlocksUnsuccessful(hasSelectedRow, hasSelectedColumn, row, column);
                hasSelectedCount = 1;
                hasSelectedRow = row;
                hasSelectedColumn = column;
            }
        }
    }

    @Override
    public int typeForBlockAtRowAndColumn(int row, int column) {
        return mainModel.getTypeOfRowAndColumn(row, column);
    }

    @Override
    public int rowNumber() {
        return mainModel.getRowNumber();
    }

    @Override
    public int columnNumber() {
        return mainModel.getColumnNumber();
    }
}
