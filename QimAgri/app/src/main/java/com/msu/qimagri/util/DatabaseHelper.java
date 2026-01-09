package com.msu.qimagri.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QimAgri.db";
    private static final int DATABASE_VERSION = 9; // Incremented version to fix downgrade crash

    // User table
    public static final String TABLE_USER = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_IMAGE = "image";

    // Combined Pest and Disease table
    public static final String TABLE_PEST_DISEASE = "pests_and_diseases";
    public static final String COLUMN_PD_ID = "id";
    public static final String COLUMN_PD_NAME = "name";
    public static final String COLUMN_PD_TYPE = "type";
    public static final String COLUMN_PD_IMAGE = "image";
    public static final String COLUMN_PD_DESCRIPTION = "description";
    public static final String COLUMN_PD_NATURAL_TREATMENT_ID = "natural_treatment_id";

    // Natural treatment table
    public static final String TABLE_NATURAL_TREATMENT = "natural_treatments";
    public static final String COLUMN_TREATMENT_ID = "id";
    public static final String COLUMN_TREATMENT_NAME = "name";
    public static final String COLUMN_TREATMENT_IMAGE = "image";
    public static final String COLUMN_TREATMENT_DESCRIPTION = "description";
    public static final String COLUMN_TREATMENT_PEST_DISEASE_ID = "pest_and_disease_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_PEST_DISEASE_TABLE = "CREATE TABLE " + TABLE_PEST_DISEASE + "("
                + COLUMN_PD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PD_NAME + " TEXT,"
                + COLUMN_PD_TYPE + " TEXT,"
                + COLUMN_PD_IMAGE + " TEXT,"
                + COLUMN_PD_DESCRIPTION + " TEXT,"
                + COLUMN_PD_NATURAL_TREATMENT_ID + " INTEGER" + ")";
        db.execSQL(CREATE_PEST_DISEASE_TABLE);

        String CREATE_NATURAL_TREATMENT_TABLE = "CREATE TABLE " + TABLE_NATURAL_TREATMENT + "("
                + COLUMN_TREATMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TREATMENT_NAME + " TEXT,"
                + COLUMN_TREATMENT_IMAGE + " TEXT,"
                + COLUMN_TREATMENT_DESCRIPTION + " TEXT,"
                + COLUMN_TREATMENT_PEST_DISEASE_ID + " INTEGER" + ")";
        db.execSQL(CREATE_NATURAL_TREATMENT_TABLE);

        addSampleUser(db);
        addPestsAndDiseases(db);
        addNaturalTreatments(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEST_DISEASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NATURAL_TREATMENT);
        onCreate(db);
    }

    private void addSampleUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, "Test User");
        values.put(COLUMN_USER_EMAIL, "test@example.com");
        values.put(COLUMN_USER_PHONE, "123-456-7890");
        values.put(COLUMN_USER_PASSWORD, "password");
        values.putNull(COLUMN_USER_IMAGE); // No image for sample user
        db.insert(TABLE_USER, null, values);
    }

    private void addPestsAndDiseases(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Pests
        values.put(COLUMN_PD_NAME, "Cabbage Worm");
        values.put(COLUMN_PD_TYPE, "Pest");
        values.put(COLUMN_PD_DESCRIPTION, "Cabbage worms are the larval stage of the cabbage white butterfly (Pieris rapae), commonly found in cruciferous crops such as cabbage, broccoli, and kale. These pests are identifiable by their velvety green bodies and slow crawling movement along leaf surfaces. Cabbage worms feed voraciously on foliage, creating irregular holes and skeletonized leaves, which significantly reduce crop quality and marketability. Their feeding habits also expose plants to secondary infections and environmental stress. Eggs are typically laid on the underside of leaves, and larvae emerge within days, making early detection crucial. Infestations often peak during warm seasons, especially in open-field cultivation. The lifecycle of cabbage worms includes egg, larva, pupa, and adult stages, with multiple generations per year in tropical climates. Their persistence and rapid reproduction make them a major concern for vegetable farmers.");
        values.put(COLUMN_PD_IMAGE, "cabbage_worm");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 2);
        db.insert(TABLE_PEST_DISEASE, null, values);

        values.clear();
        values.put(COLUMN_PD_NAME, "Grasshoppers");
        values.put(COLUMN_PD_TYPE, "Pest");
        values.put(COLUMN_PD_DESCRIPTION, "Grasshoppers are chewing insects belonging to the order Orthoptera, known for their powerful hind legs and ability to leap long distances. They pose a serious threat to a wide range of crops including rice, maize, and vegetables. Grasshoppers feed on leaves, stems, and even developing fruits, often stripping entire fields during outbreaks. Their damage is characterized by jagged leaf edges, defoliation, and reduced photosynthetic capacity. Grasshopper populations tend to surge during dry spells followed by sudden rains, which create ideal breeding conditions. Eggs are laid in soil and hatch into nymphs that resemble adults but lack wings. These nymphs undergo several molts before reaching maturity. Swarming behavior in certain species can lead to large-scale agricultural losses, especially in monoculture systems. Their mobility and feeding range make them difficult to contain once established.");
        values.put(COLUMN_PD_IMAGE, "grasshoppers");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 3);
        db.insert(TABLE_PEST_DISEASE, null, values);

        values.clear();
        values.put(COLUMN_PD_NAME, "Rats");
        values.put(COLUMN_PD_TYPE, "Pest");
        values.put(COLUMN_PD_DESCRIPTION, "Rats are omnivorous rodents that pose significant challenges in both field and post-harvest agricultural settings. They are known to damage crops such as rice, maize, and oil palm by gnawing on stems, fruits, and grains. In storage facilities, rats contaminate food supplies with droppings, urine, and hair, leading to economic losses and health risks. Their nocturnal behavior and ability to reproduce rapidly make them difficult to detect until damage becomes severe. Rats often burrow near irrigation channels or field edges, creating nests that support large colonies. They are also vectors of diseases such as leptospirosis and salmonellosis, which can affect both livestock and humans. Their adaptability to various environments, including urban and rural farms, contributes to their persistence. Effective management requires understanding their nesting habits, feeding behavior, and seasonal activity patterns.");
        values.put(COLUMN_PD_IMAGE, "rats");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 4);
        db.insert(TABLE_PEST_DISEASE, null, values);

        // Diseases
        values.clear();
        values.put(COLUMN_PD_NAME, "Anthracnose");
        values.put(COLUMN_PD_TYPE, "Disease");
        values.put(COLUMN_PD_DESCRIPTION, "Anthracnose is a fungal disease caused by various species of Colletotrichum, affecting fruits, vegetables, and ornamental plants. It is most prevalent in warm, humid climates and commonly impacts crops such as chili, mango, papaya, and beans. Symptoms include dark, sunken lesions on leaves, stems, and fruits, often with concentric rings or spore masses. Infected fruits may rot prematurely, reducing market value and shelf life. The disease spreads through rain splash, wind, and contaminated tools, especially during periods of high humidity. Spores can survive in plant debris and soil, making crop rotation and sanitation essential for prevention. Anthracnose can lead to significant yield losses if not identified early, particularly in fruiting stages. Its rapid spread and ability to infect multiple plant parts make it one of the most destructive fungal diseases in tropical agriculture.");
        values.put(COLUMN_PD_IMAGE, "anthracnose");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 1);
        db.insert(TABLE_PEST_DISEASE, null, values);

        values.clear();
        values.put(COLUMN_PD_NAME, "Bacterial Leaf Spot");
        values.put(COLUMN_PD_TYPE, "Disease");
        values.put(COLUMN_PD_DESCRIPTION, "Bacterial leaf spot is a disease caused by various bacterial pathogens, including Xanthomonas and Pseudomonas species. It affects a wide range of crops such as tomato, pepper, lettuce, and leafy greens. The disease manifests as small, water-soaked lesions that enlarge and turn brown or black, often surrounded by yellow halos. In severe cases, lesions coalesce, leading to leaf blight and premature defoliation. Bacterial leaf spot spreads through splashing water, contaminated tools, and infected seeds. High humidity and warm temperatures accelerate its development. The bacteria can survive on crop residues and in irrigation systems, making sanitation and seed selection critical. Yield losses occur due to reduced photosynthesis, poor fruit development, and increased vulnerability to secondary infections. Early detection is essential to prevent widespread damage, especially in densely planted systems.");
        values.put(COLUMN_PD_IMAGE, "bacterial_leaf_spot");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 5);
        db.insert(TABLE_PEST_DISEASE, null, values);

        values.clear();
        values.put(COLUMN_PD_NAME, "Rusts");
        values.put(COLUMN_PD_TYPE, "Disease");
        values.put(COLUMN_PD_DESCRIPTION, "Rust diseases are caused by various species of fungi in the order Pucciniales, affecting cereals, legumes, and horticultural crops. Common rusts include leaf rust, stem rust, and stripe rust, each named for the plant part they infect. Symptoms include powdery pustules that range in color from orange to brown, appearing on leaves, stems, and sometimes fruits. These pustules rupture the epidermis, releasing spores that spread rapidly via wind. Rust infections reduce photosynthetic efficiency, weaken plant structure, and lead to premature leaf drop. The disease thrives in moist environments with moderate temperatures, especially in regions with frequent dew or rainfall. Rust fungi often require alternate hosts to complete their lifecycle, complicating control strategies. Yield losses can be substantial, particularly in staple crops like wheat and soybean. Monitoring and early identification are crucial to minimize economic impact");
        values.put(COLUMN_PD_IMAGE, "rusts");
        values.put(COLUMN_PD_NATURAL_TREATMENT_ID, 6);
        db.insert(TABLE_PEST_DISEASE, null, values);
    }

    private void addNaturalTreatments(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TREATMENT_NAME, "Neem Oil Spray");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Anthracnose, a fungal disease causing dark lesions on leaves and fruits, can be managed effectively with neem oil. Extracted from neem seeds, neem oil contains azadirachtin, a compound with antifungal and insecticidal properties. When sprayed on infected plants, neem oil disrupts fungal spore germination and prevents further spread. It also strengthens plant immunity by acting as a protective barrier. Farmers typically dilute neem oil with water and apply it weekly during humid seasons when anthracnose thrives. Neem oil is biodegradable, safe for pollinators, and doesn’t leave harmful residues. Its dual action—fungal suppression and insect deterrence—makes it versatile. However, consistency is key: irregular application reduces effectiveness. Neem oil also works best preventively, so farmers should spray before visible symptoms worsen.");
        values.put(COLUMN_TREATMENT_IMAGE, "neem_oil_spray");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 4);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);

        values.clear();
        values.put(COLUMN_TREATMENT_NAME, "Bacillus Thuringiensis");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Bacillus thuringiensis (Bt) is a naturally occurring soil bacterium that produces proteins toxic to caterpillars like cabbage worms. When sprayed on cabbage leaves, the worms ingest Bt spores and proteins, which disrupt their digestive system, causing them to stop feeding and eventually die. Unlike chemical pesticides, Bt is highly specific: it targets caterpillars but leaves beneficial insects (like bees and ladybugs) unharmed. Farmers often apply Bt as a foliar spray during early infestation stages. Its effectiveness depends on timing—worms must ingest it while actively feeding. The beauty of Bt lies in its eco-friendliness: it biodegrades quickly, leaving no harmful residues, and reduces the risk of resistance compared to synthetic chemicals. However, repeated applications may be necessary since sunlight and rain degrade Bt rapidly.");
        values.put(COLUMN_TREATMENT_IMAGE, "bacillus_thuringiensis");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 1);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);

        values.clear();
        values.put(COLUMN_TREATMENT_NAME, "Nosema Locustae (Nolo bait)");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Nosema locustae (Nolo Bait) is a protozoan parasite used as a biological control against grasshoppers. Farmers spread Nolo Bait—grain carriers inoculated with Nosema spores—across fields. Grasshoppers consume the bait, and the spores infect their gut, weakening them and reducing reproduction. Over time, infected grasshoppers spread the parasite to others, creating a natural population decline. Unlike chemical sprays, Nosema doesn’t kill instantly; instead, it works gradually, aligning with ecological balance. This slow action is actually beneficial, as it prevents sudden pest explosions while allowing predators (birds, frogs) to keep populations in check. Farmers must apply Nolo Bait early in the season when grasshoppers are young, ensuring maximum spread. It’s safe for humans, livestock, and non-target species, making it a sustainable long-term solution.");
        values.put(COLUMN_TREATMENT_IMAGE, "nolo_bait");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 2);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);

        values.clear();
        values.put(COLUMN_TREATMENT_NAME, "Peppermint Oil");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Peppermint oil is a simple yet powerful natural deterrent for rats. Its strong menthol scent overwhelms rodents’ sensitive olfactory systems, making areas sprayed or treated with peppermint oil inhospitable. Farmers can soak cotton balls in peppermint oil and place them near grain storage, barns, or entry points. Alternatively, diluted peppermint oil sprays can be applied around crops or storage facilities. Unlike poisons, peppermint oil doesn’t kill rats but repels them, reducing contamination risks in food storage. It’s safe for humans and livestock, and it doubles as an insect repellent. However, its effectiveness depends on regular reapplication since the scent fades. Peppermint oil works best as part of an integrated strategy—combined with hygiene practices like sealing holes and securing feed. Its eco-friendly nature makes it ideal for smallholder farms aiming to avoid toxic rodenticides.");
        values.put(COLUMN_TREATMENT_IMAGE, "peppermint_oil");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 3);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);

        values.clear();
        values.put(COLUMN_TREATMENT_NAME, "Copper-Based Spray");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Copper-based sprays are one of the oldest and most reliable natural treatments for bacterial leaf spot. Copper ions disrupt bacterial enzymes and cell membranes, preventing multiplication. Farmers apply copper hydroxide or copper sulfate sprays directly to infected leaves, creating a protective coating that halts bacterial spread. Unlike antibiotics, copper doesn’t create resistance issues, making it sustainable for long-term use. However, dosage matters: excessive copper can harm plants and soil microbes. Farmers must follow recommended concentrations and avoid overuse. Copper sprays are most effective when applied early, at the first signs of spotting, and combined with sanitation practices like removing infected leaves. Their strength lies in their broad-spectrum activity, controlling not just bacterial leaf spot but also other fungal and bacterial diseases.");
        values.put(COLUMN_TREATMENT_IMAGE, "copper_based_spray");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 5);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);

        values.clear();
        values.put(COLUMN_TREATMENT_NAME, "Baking Soda Spray");
        values.put(COLUMN_TREATMENT_DESCRIPTION, "Rusts are fungal diseases that thrive in humid conditions, producing orange or brown pustules on leaves. A simple baking soda spray (sodium bicarbonate mixed with water and a small amount of soap) alters the leaf surface pH, making it hostile to fungal spores. The alkaline environment disrupts spore germination and slows disease progression. Farmers can prepare a solution (about 1 teaspoon baking soda per liter of water) and spray weekly during rust outbreaks. Baking soda is cheap, safe, and widely available, making it accessible for small-scale farmers. It doesn’t kill rust fungi outright but suppresses their spread, buying time for plants to recover. Its effectiveness increases when combined with cultural practices like pruning for airflow and avoiding overhead irrigation. The main advantage is its simplicity—farmers don’t need specialized products, just household baking soda.");
        values.put(COLUMN_TREATMENT_IMAGE, "baking_soda_spray");
        values.put(COLUMN_TREATMENT_PEST_DISEASE_ID, 6);
        db.insert(TABLE_NATURAL_TREATMENT, null, values);
    }
}
