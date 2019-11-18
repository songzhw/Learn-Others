/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Extends [SwipeRefreshLayout] to support non-direct descendant scrolling views.
 *
 *
 * [SwipeRefreshLayout] works as expected when a scroll view is a direct child: it triggers
 * the refresh only when the view is on top. This class adds a way (@link #setScrollUpChild} to
 * define which view controls this behavior.
 */
class ScrollChildSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    var scrollUpChild: View? = null

    override fun canChildScrollUp() =
        scrollUpChild?.canScrollVertically(-1) ?: super.canChildScrollUp()
}

/*
szw: 更详细的说明可以见 https://www.jianshu.com/p/ddb91b14c36f

当child view可以下拉时，则优先滑动child view，当child view滑到顶部时，则触发SwipeRefreshLayout 的下拉刷新。
但是当SwipeRefreshLayout 中包含多个child view时，则可能会失效，原因是SwipeRefreshLayout只会通过child view 中最上面的一个view来处理滑动事件。

看SwipeRefreshLayout的源码知道, 其mTarget其实就是第一个子view. 而上面的" super.canChildScrollUp()", 其实就是看mTarget给否往上滑动

所以本类, 其实就是不是只看第一个child view, 而是你可以指定要看谁是否可以canScrollUp().
以TasksFragment为例, 它的child views有tv来说明"All Tasks", 再下面就是rv. 而这里是指定srLayout来看rv是否可以scrollUp()
 */



/*
View.canScrollVertically(direction):
    //
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     //
    public boolean canScrollVertically(int direction) { ... }
 */