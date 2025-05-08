package doodlejump;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class Generation {

    public static void spawnPlatforms(int platformCount, int screenWidth, int startY, int endY) {
        Random random = new Random();

        // Рассчитываем высоту области спавна
        int spawnHeight = Math.abs(endY - startY);

        // Определяем количество кластеров (групп платформ)
        int clusterCount = Math.max(2, platformCount / 4);
        int platformsPerCluster = platformCount / clusterCount;

        for (int cluster = 0; cluster < clusterCount; cluster++) {
            // Базовая Y-координата для кластера
            float clusterBaseY = startY + (spawnHeight * cluster / (float)clusterCount);

            // Добавляем случайное смещение для всего кластера
            clusterBaseY += random.nextFloat() * (spawnHeight / (float)clusterCount) * 0.6f;

            for (int i = 0; i < platformsPerCluster; i++) {
                // X-координата - случайная в пределах ширины экрана
                int x = random.nextInt(screenWidth - 40) + 20;

                // Y-координата - в пределах кластера с небольшим разбросом
                int y = (int)(clusterBaseY + random.nextFloat() * (spawnHeight / (float)clusterCount) * 0.8f);

                // Если нужно инвертировать направление (если startY > endY)
                if (startY > endY) {
                    y = startY - (y - startY);
                }

                spawn("platform", x, y);
            }
        }

        // Добавляем оставшиеся платформы (если platformCount не делится на clusterCount)
        int remainingPlatforms = platformCount - (platformsPerCluster * clusterCount);
        for (int i = 0; i < remainingPlatforms; i++) {
            int x = random.nextInt(screenWidth - 40) + 20;
            int y = startY + random.nextInt(spawnHeight);
            if (startY > endY) {
                y = startY - random.nextInt(spawnHeight);
            }
            spawn("platform", x, y);
        }
    }}
