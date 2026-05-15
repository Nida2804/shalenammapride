package com.example.shalenammapride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF1976D2),
                    secondary = Color(0xFF42A5F5),
                ),
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

private const val DB_URL = "https://shalenammapride-6b847-default-rtdb.firebaseio.com/"

@Composable
fun App() {
    val nav = rememberNavController()
    var lang by remember { mutableStateOf("EN") }

    NavHost(navController = nav, startDestination = "home") {
        composable("home") { Home(nav, lang) { lang = it } }
        composable("meal") { MealScreen(lang, nav) }
        composable("gallery") { GalleryScreen(lang, nav) }
        composable("students") { StudentScreen(lang, nav) }
        composable("feedback") { FeedbackScreen(lang, nav) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(nav: NavHostController, lang: String, onLang: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (lang == "EN") "Shale Namma Pride" else "ಶಾಲೆ ನಮ್ಮ ಹೆಮ್ಮೆ", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = { onLang(if (lang == "EN") "KN" else "EN") }) {
                        Text(if (lang == "EN") "ಕನ್ನಡ" else "English", fontWeight = FontWeight.Medium)
                    }
                },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (lang == "EN") "School Transparency Portal" else "ಶಾಲಾ ಪಾರದರ್ಶಕತೆ ಪೋರ್ಟಲ್",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (lang == "EN") "Building trust between school and parents." 
                    else "ಶಾಲೆ ಮತ್ತು ಪೋಷಕರ ನಡುವೆ ನಂಬಿಕೆ ನಿರ್ಮಾಣ.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                HomeItem(
                    title = if (lang == "EN") "Today's Mid-day Meal" else "ಇಂದಿನ ಮಧ್ಯಾಹ್ನದ ಊಟ",
                    icon = Icons.Default.Restaurant,
                ) { nav.navigate("meal") }
            }

            item {
                HomeItem(
                    title = if (lang == "EN") "Our Facilities" else "ನಮ್ಮ ಸೌಲಭ್ಯಗಳು",
                    icon = Icons.Default.School,
                ) { nav.navigate("gallery") }
            }

            item {
                HomeItem(
                    title = if (lang == "EN") "Student Achievements" else "ವಿದ್ಯಾರ್ಥಿ ಸಾಧನೆಗಳು",
                    icon = Icons.Default.Star,
                ) { nav.navigate("students") }
            }

            item {
                HomeItem(
                    title = if (lang == "EN") "Parent Feedback" else "ಪೋಷಕರ ಅಭಿಪ್ರಾಯ",
                    icon = Icons.Default.Feedback,
                ) { nav.navigate("feedback") }
            }
            
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun HomeItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(20.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(lang: String, nav: NavHostController) {
    var menu by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(value = true) }

    LaunchedEffect(Unit) {
        val ref = FirebaseDatabase.getInstance(DB_URL).getReference("dailyMeal")
        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    menu = snapshot.child("menu").value?.toString() ?: ""
                    image = snapshot.child("image").value?.toString() ?: ""
                    date = snapshot.child("date").value?.toString() ?: ""
                    loading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    loading = false
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == "EN") "Daily Meal" else "ಇಂದಿನ ಊಟ") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (date.isNotEmpty()) {
                    Text(date, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Spacer(Modifier.height(12.dp))
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        if (image.isNotEmpty() && (image != "null")) {
                            AsyncImage(
                                model = image,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(240.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(Modifier.padding(20.dp)) {
                            Text(
                                if (lang == "EN") "Today's Menu" else "ಇಂದಿನ ಮೆನು",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = if ((menu.isEmpty()) || (menu == "null")) (if (lang == "EN") "Menu not updated yet." else "ಇನ್ನೂ ಅಪ್‌ಡೇಟ್ ಮಾಡಿಲ್ಲ.") else menu,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(lang: String, nav: NavHostController) {
    var list by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var loading by remember { mutableStateOf(value = true) }

    LaunchedEffect(Unit) {
        FirebaseDatabase.getInstance(DB_URL).getReference("facilities")
            .get().addOnSuccessListener {
                val temp = mutableListOf<Pair<String, String>>()
                it.children.forEach { child ->
                    temp.add(
                        child.child("title").value.toString() to
                        child.child("image").value.toString()
                    )
                }
                list = temp
                loading = false
            }.addOnFailureListener { loading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == "EN") "Facilities" else "ಸೌಲಭ್ಯಗಳು") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(list) {
                    Card(shape = RoundedCornerShape(16.dp)) {
                        Column {
                            AsyncImage(
                                model = it.second,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                it.first,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(lang: String, nav: NavHostController) {
    var list by remember { mutableStateOf(listOf<Triple<String, String, String>>()) }
    var loading by remember { mutableStateOf(value = true) }

    LaunchedEffect(Unit) {
        FirebaseDatabase.getInstance(DB_URL).getReference("students")
            .get().addOnSuccessListener {
                val temp = mutableListOf<Triple<String, String, String>>()
                it.children.forEach { child ->
                    temp.add(
                        Triple(
                            child.child("name").value.toString(),
                            child.child("achievement").value.toString(),
                            child.child("image").value.toString()
                        )
                    )
                }
                list = temp
                loading = false
            }.addOnFailureListener { loading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == "EN") "Student Stars" else "ವಿದ್ಯಾರ್ಥಿ ತಾರೆಗಳು") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(list) {
                    OutlinedCard(shape = RoundedCornerShape(16.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = it.third,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(it.first, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(it.second, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(lang: String, nav: NavHostController) {
    var text by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(value = false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (lang == "EN") "Feedback" else "ಅಭಿಪ್ರಾಯ") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Text(
                if (lang == "EN") "We value your suggestions to improve our school." 
                else "ನಮ್ಮ ಶಾಲೆಯನ್ನು ಸುಧಾರಿಸಲು ನಿಮ್ಮ ಸಲಹೆಗಳು ನಮಗೆ ಮುಖ್ಯ.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                placeholder = { Text(if (lang == "EN") "Write here..." else "ಇಲ್ಲಿ ಬರೆಯಿರಿ...") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
                Text(if (lang == "EN") "Submit Anonymously" else "ಅನಾಮಧೇಯವಾಗಿ ಸಲ್ಲಿಸಿ")
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        val ref = FirebaseDatabase.getInstance(DB_URL).getReference("feedback").push()
                        val data = mapOf(
                            "text" to text,
                            "anonymous" to isAnonymous,
                            "timestamp" to ServerValue.TIMESTAMP
                        )
                        ref.setValue(data)
                        text = ""
                        nav.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (lang == "EN") "Submit Feedback" else "ಅಭಿಪ್ರಾಯ ಸಲ್ಲಿಸಿ")
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
