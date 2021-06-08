package ir.iais.needToDelete;

/**
 * @author vahid
 * create on 6/6/2021
 */
public class PersianDigitConvertor {

    public static String convertToPersianDigit(String value) {
        if (value == null) {
            return value;
        }
        char[] str = value.toCharArray();
        for (int i = 0; i < str.length; i++) {
            char b = str[i];
            switch ((char) b) {
                case '1':
                    str[i] = '۱';
                    break;
                case '2':
                    str[i] = '۲';
                    break;
                case '3':
                    str[i] = '۳';
                    break;
                case '4':
                    str[i] = '۴';
                    break;
                case '5':
                    str[i] = '۵';
                    break;
                case '6':
                    str[i] = '۶';
                    break;
                case '7':
                    str[i] = '۷';
                    break;
                case '8':
                    str[i] = '۸';
                    break;
                case '9':
                    str[i] = '۹';
                    break;
                case '0':
                    str[i] = '۰';
                    break;
                case '.':
                    str[i] = '/';
                    break;
            }

        }
        return new String(str);
    }

    public static String convertFromPersianDigit(String value) {
        if (value == null) {
            return value;
        }
        char[] str = value.toCharArray();
        for (int i = 0; i < str.length; i++) {
            char b = str[i];
            switch ((char) b) {
                case '۱':
                    str[i] = '1';
                    break;
                case '۲':
                    str[i] = '2';
                    break;
                case '۳':
                    str[i] = '3';
                    break;
                case '۴':
                    str[i] = '4';
                    break;
                case '۵':
                    str[i] = '5';
                    break;
                case '۶':
                    str[i] = '6';
                    break;
                case '۷':
                    str[i] = '7';
                    break;
                case '۸':
                    str[i] = '8';
                    break;
                case '۹':
                    str[i] = '9';
                    break;
                case '۰':
                    str[i] = '0';
                    break;
                case '/':
                    str[i] = '.';
                    break;
            }

        }
        return new String(str);
    }

    public static String digitToText(long number) {
        if (number > 999999999) {
            return digitToText(number / 1000000000) + ((number % 1000000000) == 0 ? " ميليارد" : " ميليارد و " + digitToText(number % 1000000000));
        } else if (number > 999999) {
            return digitToText(number / 1000000) + ((number % 1000000) == 0 ? " ميليون" : " ميليون و " + digitToText(number % 1000000));
        } else if (number > 999) {
            return digitToText(number / 1000) + ((number % 1000 == 0) ? " هزار" : " هزار و " + digitToText(number % 1000));
        } else if (number > 99) {
            if (number > 199 && number < 300) {
                if (number == 200) {
                    return "دويست";
                } else {
                    return "دويست و " + digitToText(number % 100);
                }
            } else if (number > 299 && number < 400) {
                if (number == 300) {
                    return "سيصد";
                } else {
                    return "سيصد و " + digitToText(number % 100);
                }
            } else if (number > 499 && number < 600) {
                if (number == 500) {
                    return "پانصد";
                } else {
                    return "پانصد و " + digitToText(number % 100);
                }
            } else {
                return digitToText(number / 100) + ((number % 100) == 0 ? "صد" : ("صد و " + digitToText(number % 100)));
            }
        }
        if (number == 0) {
            return "صفر";
        }
        switch ((int) number) {
            case 1:
                return "يک";
            case 2:
                return "دو";
            case 3:
                return "سه";
            case 4:
                return "چهار";
            case 5:
                return "پنج";
            case 6:
                return "شش";
            case 7:
                return "هفت";
            case 8:
                return "هشت";
            case 9:
                return "نه";
            case 10:
                return "ده";
            case 11:
                return "يازده";
            case 12:
                return "دوازده";
            case 13:
                return "سيزده";
            case 14:
                return "چهارده";
            case 15:
                return "پانزده";
            case 16:
                return "شانزده";
            case 17:
                return "هفده";
            case 18:
                return "هجده";
            case 19:
                return "نوزده";

            case 20:
                return "بيست";
            case 30:
                return "سي";
            case 40:
                return "چهل";
            case 50:
                return "پنجاه";
            case 60:
                return "شصت";
            case 70:
                return "هفتاد";
            case 80:
                return "هشتاد";
            case 90:
                return "نود";
            default:
                return digitToText((number / 10) * 10) + " و " + digitToText((number % 10));
        }
    }

}
