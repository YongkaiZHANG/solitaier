package be.kuleuven.drsolitaire.classes;

//import android.arch.persistence.room.TypeConverter;
import androidx.room.TypeConverter;


// @NG
public enum MoveType {
    MOVESTART("moveStart"),
    MOVESUCCESFUL("moveSuccessful"),
    MOVEERROR("moveError"),
    MOVEPILE("movePile"),
    MOVEUNDO("moveUndo"),
    MOVEHINT("moveHint"),
    MOVEPILEFINISHED("movePileFinished"),
    MOVETAP("moveTap");


    private String string;

    MoveType(String string) {
        this.string = string;
    }

    public String toString() {return this.string;}

    @TypeConverter
    public static MoveType getMoveType(String string){
        for(MoveType move : values()){
            if(move.string.equals(string)){
                return move;
            }
        }
        return null;
    }

    @TypeConverter
    public static String getMoveTypeString(MoveType moveType){
        return moveType.toString();
    }
}
