package com.yandex.practicum.middle_homework_5.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.yandex.practicum.middle_homework_5.data.SourceProvider
import com.yandex.practicum.middle_homework_5.data.database.entity.News
import com.yandex.practicum.middle_homework_5.ui.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = koinViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val news: LazyPagingItems<News> = appViewModel.getNews().collectAsLazyPagingItems()
    Column(modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 96.dp)) {
        Spacer(Modifier.padding(top = 32.dp))
        LazyColumn {
            items(
                count = news.itemCount,
                key = news.itemKey()
            ) { it ->
                news[it]?.let { RenderNews(it) }
            }
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                SourceProvider.pagingItems = news
                appViewModel.launchPeriodicRefresh()
            } else if (event == Lifecycle.Event.ON_STOP) {
                appViewModel.cancelPeriodicRefresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun RenderNews(item: News) {
    Box(modifier = Modifier.padding(top = 4.dp)) {
        Card {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = item.title, fontWeight = FontWeight.Bold)
                Text(text = item.details)
            }
        }
    }
}