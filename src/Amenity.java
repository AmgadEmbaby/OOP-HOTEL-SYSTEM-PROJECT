public class Amenity {

    private boolean wifi=false;
    private boolean housekeeping=false;
    private boolean LaundryService =false;
    private boolean minibar=false;
    private boolean RoomDigitalSafe =false;
    private boolean BreakfastInBed =false;
    private boolean AddExtraBed=false;
    private boolean ExtraPillows=false;
    private boolean ExtraToiletries=false;
    private boolean ExtraTowels=false;
    private boolean BathSet=false; //robe&slippers
    private boolean SewingKit=false;
    private boolean CoffeeMachine=false;
    private boolean BluetoothSpeaker=false;
    private boolean jacuzzi =false;
    private boolean spa=false;
    private boolean PrivatePool=false;
    private boolean LateCheckOut=false;


    //all prices are in dollars($)
    public static double WifiCost=10;
    public static double HousekeepingCost=25;
    public static double LaundryCost=15;
    public static double MinibarCost=25;
    public static double SafeCost=20;
    public static double BreakfastInBedCost=40;
    public static double ExtraBedCost=30;
    public static double ExtraPillowsCost=8;
    public static double ExtraToiletriesCost=10;
    public static double ExtraTowelsCost=8;
    public static double BathSetCost=12;
    public static double SewingKitCost=5;
    public static double CoffeeMachineCost=24;
    public static double BluetoothSpeakerCost=7;
    public static double JacuzziCost=135;
    public static double SpaCost=75;
    public static double PrivatePoolCost=375;
    public static double LateCheckOutCost=15;

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public void setHousekeeping(boolean housekeeping) {
        this.housekeeping = housekeeping;
    }

    public void setLaundryService(boolean laundryService) {
        LaundryService = laundryService;
    }

    public void setMinibar(boolean minibar) {
        this.minibar = minibar;
    }

    public void setRoomDigitalSafe(boolean roomDigitalSafe) {
        RoomDigitalSafe = roomDigitalSafe;
    }

    public void setBreakfastInBed(boolean breakfastInBed) {
        BreakfastInBed = breakfastInBed;
    }

    public void setAddExtraBed(boolean addExtraBed) {
        AddExtraBed = addExtraBed;
    }

    public void setExtraPillows(boolean extraPillows) {
        ExtraPillows = extraPillows;
    }

    public void setExtraToiletries(boolean extraToiletries) {
        ExtraToiletries = extraToiletries;
    }

    public void setExtraTowels(boolean extraTowels) {
        ExtraTowels = extraTowels;
    }

    public void setBathSet(boolean bathSet) {
        BathSet = bathSet;
    }

    public void setSewingKit(boolean sewingKit) {
        SewingKit = sewingKit;
    }

    public void setCoffeeMachine(boolean coffeeMachine) {
        CoffeeMachine = coffeeMachine;
    }

    public void setBluetoothSpeaker(boolean bluetoothSpeaker) {
        BluetoothSpeaker = bluetoothSpeaker;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public void setPrivatePool(boolean privatePool) {
        PrivatePool = privatePool;
    }

    public void setLateCheckOut(boolean lateCheckOut) {
        LateCheckOut = lateCheckOut;
    }

    public boolean isWifi() {
        return wifi;
    }

    public boolean isLaundryService() {
        return LaundryService;
    }

    public boolean isHousekeeping() {
        return housekeeping;
    }

    public boolean isMinibar() {
        return minibar;
    }

    public boolean isRoomDigitalSafe() {
        return RoomDigitalSafe;
    }

    public boolean isBreakfastInBed() {
        return BreakfastInBed;
    }

    public boolean isAddExtraBed() {
        return AddExtraBed;
    }

    public boolean isExtraPillows() {
        return ExtraPillows;
    }

    public boolean isExtraToiletries() {
        return ExtraToiletries;
    }

    public boolean isExtraTowels() {
        return ExtraTowels;
    }

    public boolean isBathSet() {
        return BathSet;
    }

    public boolean isSewingKit() {
        return SewingKit;
    }

    public boolean isCoffeeMachine() {
        return CoffeeMachine;
    }

    public boolean isBluetoothSpeaker() {
        return BluetoothSpeaker;
    }

    public boolean isJacuzzi() {
        return jacuzzi;
    }

    public boolean isSpa() {
        return spa;
    }

    public boolean isPrivatePool() {
        return PrivatePool;
    }

    public boolean isLateCheckOut() {
        return LateCheckOut;
    }

    public Amenity() {
    }

}

