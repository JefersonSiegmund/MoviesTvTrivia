package com.jeferson.moviestvtrivia

class Constants {
    companion object {

        fun defaultTriviaList(): ArrayList<TriviaModel> {

            val triviaList = ArrayList<TriviaModel>()

            val jumpingJacks =
                TriviaModel(1, "O primeiro-ministro (Rory Kinnear, à esquerda) perplexo após assistir um video da princesa Susannah (Lydia Wilson)", R.drawable.image1, false, false, 100, "Trivia")
            triviaList.add(jumpingJacks)

            val wallSit = TriviaModel(2, "When the pig appeared in the set..", R.drawable.image2, false, false, 8000,"Trivia")
            triviaList.add(wallSit)

            val wallSit2 = TriviaModel(3, "In the last scene the director...", null, false, false, 10000, "Backstage")
            triviaList.add(wallSit2)

            return triviaList
        }
    }
}