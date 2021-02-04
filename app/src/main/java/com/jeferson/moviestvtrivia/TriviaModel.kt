package com.jeferson.moviestvtrivia

import java.util.concurrent.TimeUnit

class TriviaModel (private var id: Int,
                   private var name: String,
                   private var image: Int?,
                   private var isCompleted: Boolean,
                   private var isSelected: Boolean,
                   private var timeShow: Long,
                   private var category: String
    ) {
        fun getId(): Int {
            return id
        }

        fun setId(id: Int) {
            this.id = id
        }

        fun getName(): String {
            return name //.padEnd(140, '-')
        }

        fun setName(name: String) {
            this.name = name
        }

        fun getImage(): Int? {
            return image
        }

        fun setImage(image: Int?) {
            this.image = image
        }

        fun getIsCompleted(): Boolean {
            return isCompleted
        }

        fun setIsCompleted(isCompleted: Boolean) {
            this.isCompleted = isCompleted
        }

        fun getIsSelected(): Boolean {
            return isSelected
        }

        fun setIsSelected(isSelected: Boolean) {
            this.isSelected = isSelected
        }

        fun getTimeShow(): Long {
            return timeShow
        }

        fun setTimeShow(timeShow: Long) {
            this.timeShow = timeShow
        }

        fun getCatTime(): String {
            return category + " • " + String.format("%2d'%02d''%02d",
                    TimeUnit.MILLISECONDS.toHours(timeShow),
                    TimeUnit.MILLISECONDS.toMinutes(timeShow) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeShow)),
                    TimeUnit.MILLISECONDS.toSeconds(timeShow) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeShow))
            )
        }

        fun setCatTime(category: String) {
            this.category = category
        }
}