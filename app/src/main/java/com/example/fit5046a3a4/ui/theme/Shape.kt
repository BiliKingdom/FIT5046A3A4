
package com.example.fit5046a3a4.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // 小组件的形状（如按钮、小卡片等）
    extraSmall = RoundedCornerShape(4.dp),
    
    // 常规组件的形状（如输入框等）
    small = RoundedCornerShape(8.dp),
    
    // 中等大小组件的形状（如对话框等）
    medium = RoundedCornerShape(12.dp),
    
    // 大型组件的形状（如底部表单等）
    large = RoundedCornerShape(16.dp),
    
    // 特大组件的形状（如全屏卡片等）
    extraLarge = RoundedCornerShape(24.dp)
)

// 自定义形状常量
object CustomShapes {
    // 顶部圆角形状（用于底部表单）
    val TopRounded = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    
    // 底部圆角形状（用于顶部下拉面板）
    val BottomRounded = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
    
    // 胶囊形状（用于标签、徽章等）
    val Capsule = RoundedCornerShape(50)
    
    // 不对称圆角（用于特殊卡片）
    val AsymmetricRounded = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 24.dp,
        bottomStart = 24.dp,
        bottomEnd = 8.dp
    )
    
    // 温和圆角（用于列表项）
    val SoftRounded = RoundedCornerShape(8.dp)
    
    // 强调圆角（用于重要卡片）
    val EmphasisRounded = RoundedCornerShape(20.dp)
    
    // 对话框形状
    val Dialog = RoundedCornerShape(28.dp)
    
    // 浮动按钮形状
    val FloatingButton = RoundedCornerShape(16.dp)
    
    // 输入框形状
    val TextField = RoundedCornerShape(12.dp)
    
    // 卡片形状
    val Card = RoundedCornerShape(16.dp)
}