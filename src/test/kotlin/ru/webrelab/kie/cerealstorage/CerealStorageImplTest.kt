package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

//    private val storage = mutableMapOf(10f, 20f)

    private lateinit var storage: CerealStorage

    @BeforeEach
    fun setup() {
        storage = CerealStorageImpl(10f, 20f)
    }

// addCereal

    @Test
    fun `добавлям крупу меньше макс объёма и проверяем, что именно такое количество добавили`() = with(storage) {
        addCereal(Cereal.MILLET, 5.5f)
        Assertions.assertEquals(5.5f, getAmount(Cereal.MILLET))
    }

    @Test
    fun `добавлям крупу меньше макс объёма несколько раз и проверяем, что именно такое количество добавили`() =
        with(storage) {
            addCereal(Cereal.MILLET, 1.1f)
            addCereal(Cereal.MILLET, 2.2f)
            Assertions.assertEquals(3.3f, getAmount(Cereal.MILLET), 0.01f)
        }

    @Test
    fun `добавлям разные крупы`() = with(storage) {
        addCereal(Cereal.RICE, 1.1f)
        addCereal(Cereal.PEAS, 2.2f)
        Assertions.assertAll({ Assertions.assertEquals(1.1f, getAmount(Cereal.RICE)) },
            { Assertions.assertEquals(2.2f, getAmount(Cereal.PEAS)) })
    }

    @Test
    fun `добавлям больше макс объёма и проверяем, возвращает ли остаток`() {
        val remaining = storage.addCereal(Cereal.RICE, 50f)
        assertEquals(40f, remaining, 0.01f)
    }

    @Test
    fun `добавлям больше макс объёма и проверяем, правильное ли количество показывает в контейнере`() {
        val remaining = storage.addCereal(Cereal.RICE, 50f)
        assertEquals(10f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `добавляем отрицательное количество и проверяем exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -1f)
        }
    }


// getCereal

    @Test
    fun `возвращает ли количество полученной крупы`() {
        val cereal = Cereal.BULGUR
        //обязательно! создаём контейнер, иначе всё падает, потому что без контейнера результат 0
        storage.addCereal(cereal, 10f)
        val received = storage.getCereal(cereal, 4f)
        assertEquals(4f, received, 0.01f)
    }

    @Test
    fun `возвращает ли остаток`() {
        val cereal = Cereal.BULGUR
        val received = storage.addCereal(cereal, 15f)
        assertEquals(5f, received, 0.01f)
    }

    @Test
    fun `вернет ли 0, если попробуем взять крупу из несуществующего контейнера`() {
        val received = storage.getCereal(Cereal.RICE, 5f)
        assertEquals(0f, received, 0.01f)
    }


// removeContainer

    @Test
    fun `удален ли контейнер, если он пустой`() {
        val cereal = Cereal.PEAS
        // здесь тоже сначала создаем контейнер, иначе всё валится
        storage.addCereal(cereal, 10f)
        storage.getCereal(cereal, 10f)
        val result = storage.removeContainer(cereal)
        assertTrue(result)
    }

    @Test
    fun `НЕ удален ли контейнер, если он НЕ пустой`() {
        val cereal = Cereal.PEAS
        // то же
        storage.addCereal(cereal, 9f)
        val result = storage.removeContainer(cereal)
        assertFalse(result)
    }

    @Test
    fun `НЕ удален ли контейнер, если его не существует`() {
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result, "Контейнера не сузествует, удаление невозможно")
    }


// getAmount

    @Test
    fun `cколько крупы в контейнере`() {
        val cereal = Cereal.MILLET
        storage.addCereal(Cereal.MILLET, 10f)
        val inStorage = storage.getAmount(cereal)
        assertEquals(10f, inStorage, 0.01f)
    }

    @Test
    fun `cколько крупы в контейнере после изъятия`() {
        val cereal = Cereal.MILLET
        storage.addCereal(cereal, 10f)
        storage.getCereal(cereal, 5f)
        val inStorage = storage.getAmount(cereal)
        assertEquals(5f, inStorage, 0.01f)
    }

    @Test
    fun `если контейнера нет, должно вернуть 0`() {
        assertEquals(0f, storage.getAmount(Cereal.RICE))
    }


    // getSpace
    @Test
    fun `сколько есть места изначально`() {
        val cereal = Cereal.BUCKWHEAT
        val storageAvailable = storage.getSpace(cereal)
        assertEquals(10f, storageAvailable)
    }

    @Test
    fun `сколько есть места после добавления`() {
        val cereal = Cereal.BUCKWHEAT
        storage.addCereal(cereal, 50f)
        val storageAvailable = storage.getSpace(cereal)
        assertEquals(0f, storageAvailable)
    }

//testToString

    @Test
    fun `правильно ли отображается количество `() {
        val storage = CerealStorageImpl(10f, 20f)
        val expected = "В вашем контейнере места: 10.0, а в хранилище 20.0."
        assertEquals(expected, storage.toString())
    }
}

