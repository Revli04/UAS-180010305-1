package com.va181.pradana;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.constraintlayout.solver.ArrayRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_AKTOR= "Aktor";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_SINOPSIS = "Sinopsis";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFromat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_AKTOR + " TEXT, "
                + KEY_GENRE+ " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());
        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL,  dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR,  dataFilm.getAktor ());
        cv.put(KEY_GENRE,  dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS,  dataFilm.getSinopsis ());
        cv.put(KEY_LINK,  dataFilm.getLink());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm ())});

        db.close();
    }

    public void  hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFromat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Film tempFilm = new Film (
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }

        return dataFilm;

    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return  location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //Menambah data film ke-1
        try {
            tempDate = sdFromat.parse("13/10/2014 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Film film1 = new Film (
                idFilm,
                "John Wick",
                tempDate,
                storeImageFile(R.drawable.film1),
                "Keanu Reeves as John Wick\n" +
                "Michael Nyqvist as Viggo Tarasov\n" +
                "Alfie Allen as Iosef Tarasov\n" +
                "Adrianne Palicki as Ms. Perkins\n" +
                "Bridget Moynahan as Helen Wick\n" +
                "Dean Winters as Avi\n" +
                "Ian McShane as Winston\n" +
                "John Leguizamo as Aurelio\n" +
                "Willem Dafoe as Marcus",
                "Action",
                "Suatu malam, John dihampiri oleh sekelompok orang bersenjata yang membuatnya babak belur. Ternyata, mereka adalah tiga pemuda yang bertemu dengan John di pom bensin. Mereka lalu mencuri mobil John dan membunuh anak anjingnya. John Wick pun marah dan langsung melacak keberadaan mereka bertiga.\n" +
                        "Dari situlah diketahui bahwa John Wick adalah orang yang sangat berbahaya jika kehidupannya diusik. Ia adalah mantan pembunuh bayaran terbaik yang pernah dimiliki oleh sindikat milik ayah dari pemuda yang membunuh anjingnya. Akhirnya, ia pun berniat untuk mengejar para pemuda itu sekaligus menghabisi kelompok mafia Rusia yang pernah menaungi John itu.\n" +
                        "Beragam adegan laga yang melibatkan senjata api ditampilkan secara intens dalam film ini. Bahkan jika kita membandingkannya dengan judul-judul film action lain yang pernah dimainkan Keanu Reeves, John Wick terbilang paling realistis dalam menyajikan berbagai adegan seru tanpa adanya hal-hal berbau fiksi.\n" +
                        "Kelebihan film ini juga jelas terlihat dengan dihadirkannya alur yang cukup padat namun tidak lambat. Meskipun tokoh yang dihadirkan cukup banyak, namun penonton rasanya tidak akan masalah jika melewatkan nama-nama maupun latar belakang mereka.\n" +
                        "Keanu Reeves sebelumnya memang sempat populer sejak memberikan penampilan maksimalnya dalam Speed serta trilogi The Matrix. Namun melalui John Wick, kita bisa melihat watak tokoh sekaligus ekspresi Reeves yang bisa dibilang sangat selaras. Akan tetapi, film ini tergolong sangat lemah dalam menghadirkan unsur drama percintaan antara John dengan istrinya yang ditampilkan terlalu cepat di bagian awal dan akhir. Beberapa adegan juga terlalu memberi kesan kalau John Wick adalah manusia super yang tak terkalahkan.",
                "https://www.youtube.com/watch?v=2AUmvWm5ZDQ"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data film ke-2
        try {
            tempDate = sdFromat.parse("27/04/2008 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film (
                idFilm,
                "Wanted",
                tempDate,
                storeImageFile(R.drawable.film2),
                "James McAvoy sebagai Wesley Allan Gibson\n" +
                "Morgan Freeman sebagai Sloan\n" +
                "Angelina Jolie sebagai Fox\n" +
                "Thomas Kretschmann sebagai Cross\n" +
                "Common sebagai Earl Malcolm Spellman\n" +
                "Konstantin Khabensky sebagai The Exterminator\n" +
                "Marc Warren sebagai The Repairman* Dato Bakhtadze sebagai The Butcher\n" +
                "Terence Stamp sebagai Pekwarsky",
                "Action",
                "Wesley Gibson (James McAvoy) bukanlah siapa-siapa. Ia hanyalah seorang karyawan lugu yang sering jadi bulan-bulanan atasan dan teman kerjanya. Lebih buruk lagi, pacar Wesley bahkan selingkuh dengan teman kerja Wesley sementara Wesley tak mampu berbuat apa-apa. Namun hidup Wesley dalam sekejap mata berubah setelah ia bertemu Fox (Angelina Jolie). Wesley bertemu Fox saat ia sedang membeli obat untuk meredakan rasa gelisahnya. Dalam pembicaraan singkat, Fox mengatakan bahwa ayah Wesley sebenarnya tidak meninggal saat Wesley baru berusia 7 hari. Ayah Wesley adalah sorang pembunuh bayaran yang baru saja meninggal sehari sebelumnya dan bahwa Wesley adalah target berikutnya.\n" +
                        "Sebelum sempat berpikir, Cross (Thomas Kretschmann), orang yang membunuh ayah Wesley datang untuk membunuh Wesley. Untungnya Fox mampu mengatasi keadaan dan membawa Wesley yang masih kebingungan ke markas Persaudaraan. Pemimpin kelompok itu, Sloan, menjelaskan bahwa serangan panik Wesley sebenarnya ekspresi terlatih dari kemampuan manusia super langka, saat stres, denyut jantung meningkat secara drastis dan adrenalin menghasilkan tingkat dalam semburan kekuatan super, kecepatan, dan refleks - terlihat ketika ia berhasil menembak sayap dari lalat. Persaudaraan bisa mengajarinya untuk mengontrol kemampuan ini, sehingga Wesley dapat mengikuti jejak ayahnya sebagai seorang pembunuh, dimulai dengan mewarisi kekayaannya. Wesley awalnya menolak dan kembali bekerja, ketika akhirnya menemukan beberapa juta dolar dalam rekening bank. Dia mengkritik bosnya di depan seluruh pegawai kantor, dan saat berjalan keluar, dia memukul wajah Barry dengan keyboard komputer. Fox menunggu di luar untuk membawanya kembali ke markas Persaudaraan-sebuah pabrik tekstil sederhana.\n" +
                        "Setelah misi rutin beberapa kesempatan pertemuan dengan Cross, di mana Wesley ditembak di lengan dengan peluru, Sloan memberikan misi Wesley yang ingin membalaskan dendam ayahnya dan mengirimkan dia untuk membunuh Cross-tapi kemudian diam-diam memberikan Fox misi untuk membunuh Wesley, mengatakan bahwa namanya muncul dalam mesin tenun juga. Wesley menganalisis peluru yang menghantamnya, ditemukan bahwa produsen itu Pekwarsky, yang tinggal pembuat peluru di bagian timur Moravia. Wesley dan Fox perjalanan di sana dan menangkap Pekwarsky, yang mengatur pertemuan dengan Cross. Wesley melihat Cross sendirian di kereta yang berangkat. Fox mencuri mobil dan menabrakannya ke kereta, akhirnya menyebabkan suatu penggelinciran. Setelah Cross menyelamatkan hidup Wesley dengan mencegah dia dari jatuh ke dalam jurang, Wesley menembaknya. Sebelum mati, Cross menyatakan bahwa dia adalah ayah kandung Wesley. Fox menegaskan hal ini, dan menjelaskan bahwa Wesley direkrut karena ia satu-satunya orang yang tidak bisa dibunuh Cross. Fox kemudian mengungkapkan urutan membunuh pada Wesley dan mengangkat pistolnya, tetapi Wesley lolos dengan menembak kaca bawahnya dan terjun ke sungai di bawah.\n" +
                        "Wesley diambil oleh Pekwarsky, yang membawanya ke apartemen ayahnya, terletak di seberang jalan dari rumah tua Wesley. Pekwarsky menjelaskan bahwa Sloan mulai menargetkan keuntungan setelah menemukan bahwa ia ditargetkan oleh mesin tenun takdir, dan tidak memberitahu anggota Persaudaraan bahwa mereka sekarang tidak lebih dari pembunuh bayaran. Cross menemukan kebenaran, dan mulai membunuh anggota Persaudaraan untuk menjauhkan mereka dari anaknya. Pekwarsky membeli tiket pesawat, dan menngatakan bahwa Cross berharap Wesley hidup bebas dari kekerasan. Namun, Wesley memutuskan untuk membunuh Sloan setelah menemukan sebuah ruangan rahasia yang mengandung semua senjata dan peta ayahnya.",
                 "https://www.youtube.com/watch?v=edpEspHOeVU"
                );

        tambahFilm(film2, db);
        idFilm++;

        //Data film ke-3
        try {
            tempDate = sdFromat.parse("28/11/2012 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film (
                idFilm,
                "Life Of Pi",
                tempDate,
                storeImageFile(R.drawable.film3),
                "Irrfan Khan sebagai Piscine Molitor Patel\n" +
                "Rafe Spall sebagai Yann Martel\n" +
                "Tabu sebagai Gita Patel, ibu Pi\n" +
                "Adil Hussain sebagai Santosh Patel, ayah Pi\n" +
                "Gérard Depardieu sebagai koki\n" +
                "Bo-Chieh Wang sebagai pelaut\n" +
                "Shravanthi Sainath sebagai Anandi, kekasih Pi\n" +
                "Andrea Di Stefano sebagai pendeta\n" +
                "Vibish Sivakumar sebagai Ravi Patel, kakak Pi",
                "Dokumenter",
                "Ayah Pi, Santosh (Adil Hussain), menamainya Piscine Molitor dari kolam renang di Prancis. Di sekolah menengah di Pondicherry, ia mengadopsi nama Pi (huruf Yunani, π) untuk menghindari julukan yang mirip dengan suara Pissing Patel. Ia dibesarkan dalam keluarga Hindu, tetapi pada usia 12 tahun, ia diperkenalkan ke agama Katolik dan kemudian Islam, lalu ia memutuskan untuk mengikuti ketiga agama tersebut karena ia hanya ingin mencintai Tuhan. Ibunya, Gita (Tabu), mendukung keinginannya untuk tumbuh, tetapi ayahnya yang rasionalis mengajarinya tentang duniawi dan kenyataan. Keluarga Pi memiliki kebun binatang dan Pi tertarik pada hewan, terutama seekor harimau benggala bernama Richard Parker. Setelah Pi menjadi sangat dekat dengan Richard Parker, ayahnya memaksanya untuk menyaksikan harimau itu membunuh seekor kambing.\n" +
                "Ketika Pi berusia 16 tahun, ayahnya menyampaikan bahwa mereka harus pindah ke Kanada, di mana ia berniat untuk menetap dan menjual hewan-hewan tersebut. Keluarga Pi memesan tempat beserta para hewan tersebut ke kapal barang Jepang. Ketika badai, kapal barang tersebut karam sementara Pi berada di dek. Pi mencoba mencari keluarganya, tetapi seorang awak melemparnya ke sekoci. Seekor zebra yang dibebaskan melompat ke sekoci bersamanya, mematahkan kakinya. Kapal itu tenggelam ke Palung Mariana. Pi melihat isi sekoci yang tertutup itu, yang tampaknya merupakan korban selamat, tetapi ternyata Richard Parker muncul.\n" +
                "Setelah badai, Pi terbangun di sekoci dengan zebra, orang utan yang cerdas. Seekor dubuk berbintik muncul dari bawah kain terpal yang menutupi setengah sekoci dan menjebak Pi, memaksanya mundur ke ujung sekoci. Dubuk tersebut membunuh zebra dan orang utan. Richard Parker muncul dari bawah terpal, membunuh dubuk dan mencoba membunuh Pi, sebelum mundur lagi ke bawah kain terpal sekoci selama beberapa hari.\n" +
                "Pi membuat rakit pembatas kecil dari pelampung yang diambilnya demi keselamatan Richard Parker. Terlepas dari kode moralnya melawan pembunuhan, ia mulai memancing, yang memungkinkannya untuk menahan harimau tersebut. Ketika Richard Parker melompat ke laut untuk berburu ikan dan kemudian datang mengancam ke arah Pi, Pi membiarkannya tenggelam, tetapi akhirnya ia membantu Richard Parker kembali ke kapal. Suatu malam, seekor paus bungkuk menerobos sekoci, menghancurkan rakit dan persediaannya. Pi melatih Richard Parker untuk menerimanya di sekoci dan menyadari bahwa merawat harimau tersebut juga membantu menjaga dirinya tetap hidup.",
                "https://www.youtube.com/watch?v=3mMN693-F3U"

        );

        tambahFilm(film3, db);
        idFilm++;

        // Data film ke-4
        try {
            tempDate = sdFromat.parse("13/12/2014 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film4 = new Film (
                idFilm,
                "Kingsman",
                tempDate,
                storeImageFile(R.drawable.film4),
                "Taron Egerton\n" +
                "Colin Firth\n" +
                "Samuel L. Jackson\n" +
                "Mark Strong\n" +
                "Michael Caine\n" +
                "Sophie Cookson\n" +
                "Sofia Boutella\n" +
                "Samantha Womack\n" +
                "Geoff Bell\n" +
                "Edward Holcroft\n" +
                "Mark Hamill\n" +
                "Jack Davenport\n" +
                "Jack Cutmore-Scott\n" +
                "Lily Travers\n" +
                "Richard Brake",
                "Action, Crime",
                "Seorang agen rahasia bernama Harry Hart (Colin Firth) terlihat berbicara di depan pintu rumah seorang anak bernama Gary Unwin alias Eggsy (Taron Egerton), yang saat itu masih berusia lima tahun. Harry memberikan nomor teleponnya kepada Eggsy, namun hanya boleh ditelepon saat dalam keadaan sangat kacau atau genting. Selama belasan tahun, Eggsy tak pernah meneleponnya. Eggsy sendiri sebenarnya adalah putra dari teman Harry yang dulunya sama-sama menjadi anggota sebuah organisasi agen rahasia independen bernama “Kingsman”. Namun, ayah Eggsy meninggal saat menjalankan misi belasan tahun lalu di Timur Tengah. Setelah 17 tahun sejak pertemuan mereka, tiba-tiba saja Eggsy yang sudah berubah menjadi seorang pemuda menelepon Harry. Rupanya dia sedang mengalami kondisi darurat, dikejar sekelompok orang.\n" +
                        "Harry pun langsung datang dan membantu mengalahkan semua musuh Eggsy. Setelah itu, agen rahasia veteran yang handal itu pun menawari pemuda Eggsy untuk bergabung dalam program agen rahasia tingkat tinggi di “Kingsman”. Saat memutuskan untuk mengikuti tes masuk “Kingsman” itu, dia malah menjadi bahan lelucon oleh calon anggota baru lainnya. Eggsy juga terkenal serampangan, sering membuat onar, mencuri hingga ditangkap polisi. Namun, dia ternyata punya potensi, serta memiliki kemampuan dan ketangkasan di atas rata-rata layaknya agen spionase pada umumnya. Eggsy harus menjalani berbagai tes berat untuk menjadi anggota “Kingsman”, yang terkenal sebagai organisasi agen rahasia dari Inggris yang mematikan. Sejak itu, sikapnya pun berubah menjadi lebih baik, apalagi dia juga sudah diterima sebagai agen rahasia muda dan menjalani program latihan sebagai agen rahasia di “Kingsman”. Hingga pada suatu saat, Eggsy dan para agen rahasia muda lainnya mendapat tugas untuk menghadapi musuh yang sangat berbahaya berteknologi tinggi, bernama Richmond Valentine",
                "https://www.youtube.com/watch?v=kl8F-8tR8to"
                );

        tambahFilm(film4, db);
        idFilm++;

        //Data film ke-5
        try {
            tempDate = sdFromat.parse ( "11/02/2001" );
        } catch (ParseException er) {
            er.printStackTrace ();
        }
        Film film5 = new Film (
                idFilm,
                "Mr Bones",
                tempDate,
                storeImageFile ( R.drawable.film5 ),
                "Leon Schuster .... Mr. Bones\n" +
                "David Ramsay .... Vince Lee\n" +
                 "Faizon Love .... Pudbedder\n" +
                "Robert Whitehead .... Zach Devlin\n" +
                "Jane Benney .... Laleti\n" +
                "Fem Belling .... Pilot Helikopter\n" +
                 "Fats Bookholane .... Raja Tsonga\n" +
                "Zack Du Plessis .... Petani\n" +
                "Ipeleng Matlhaku .... Lindiwe\n" +
                "Jerry Mofokeng .... Sangoma\n" +
                "Craig Morris .... Menantu Futur\n" +
                "Septula Sebogodi .... Raja Muda Tsonga\n" +
                "Muso Sefatsa .... Pria Tsonga\n" +
                "Keketso Semoko .... Ibu Laleti\n" +
                "Kyle Van Zyl .... Baby Bones\n" +
                "Adam Woolf .... Young Bones",
                "Drama, Komedi",
                "Film ini berhubungan ke kerajaan Afrika, mungkin salah satu mantan tanah air, dan pencarian untuk pewaris takhta tersebut. Profesier raja, Mr Bones, perjalanan utara ke Sun City, untuk mencari putra hanya raja. Pada saat yang sama, beberapa tokoh Amerika melakukan perjalanan ke Sun City untuk Tantangan Golf Nedbank pada tahunan. Ketika Bones tiba di sana, dua dunia menabrak satu sama lain: Meskipun dia tidak bergaul dengan kehidupan modern, tokoh Amerika adalah heran tentang perilaku dan keterbelakangan negara. Masalah ekstra disebabkan oleh Bones yang pencarian untuk pangeran dan beberapa taruhan di Tantangan Golf membuat hasilnya sangat penting.",
                "https://www.youtube.com/watch?v=JKkgIAeXV2s"
        );

        tambahFilm (film5,db );

    }
}
