public class Date {
    private int day;
    private int month;
    private int year;


    public Date(int day, int month, int year) throws Exception {
        if (! isValid(day,month,year))
        {
            throw new Exception("Error : it's not a real date ");
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public boolean isValid(int d , int m , int y )
    {
        if ( y<2026 )
            return false;
        if ( m<1 || m >12 )
            return false;
        int maxdays;
        switch (m)
        {
            case 4: case 6: case 9: case 11:
                maxdays=30;
                break;

            case 2:
                maxdays=28;
                break;

            default:
                maxdays=31;
                break;
        }

        return d>=1 && d <=maxdays ;
    }

    public boolean isAfter(Date other) {
        // 1. If this year is greater, it's definitely after
        if (this.year > other.year) return true;

        // 2. If years are same, but this month is greater, it's after
        if (this.year == other.year && this.month > other.month) return true;

        // 3. If years and months are same, but this day is greater, it's after
        if (this.year == other.year && this.month == other.month && this.day > other.day) return true;

        // Otherwise, it is not after (it's either before or the same day)
        return false;
    }
}
