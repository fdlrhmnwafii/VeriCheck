import pandas as pd
import tensorflow as tf
import re
from sklearn.model_selection import train_test_split
from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences

df = pd.read_csv('merging_data.csv')

features = df['headline'] + df['content']
labels = df['hoax']
training_sentences, validation_sentences, training_labels, validation_labels = train_test_split(features, labels, train_size=.8, random_state=42)

vocab_size = 1000
embedding_dim = 16
max_length = 120
trunc_type = 'post'
padding_type = 'post'
oov_tok = "<OOV>"

tokenizer = Tokenizer(num_words=vocab_size, oov_token=oov_tok)
tokenizer.fit_on_texts(training_sentences)

training_sequences = tokenizer.texts_to_sequences(training_sentences)
training_padded = pad_sequences(training_sequences, maxlen=max_length, truncating=trunc_type, padding=padding_type)
validation_sequences = tokenizer.texts_to_sequences(validation_sentences)
validation_padded = pad_sequences(validation_sequences, maxlen=max_length, truncating=trunc_type, padding=padding_type)

model = tf.keras.Sequential([
        tf.keras.layers.Embedding(vocab_size, embedding_dim, input_length=max_length),
        tf.keras.layers.GlobalMaxPooling1D(),
        tf.keras.layers.Dense(6, activation='relu'),
        tf.keras.layers.Dense(1, activation='sigmoid')
    ])

model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
model.fit(training_padded, training_labels,
          epochs=10,
          batch_size=32,
          validation_data=(validation_padded, validation_labels))

# Input text here
text_data = """
Narasi:

“Salah Satu Mesjid di Palestina Dibom Saat Lagi Adzan”

= = =

Penjelasan:

Beredar sebuah video di Facebook yang menunjukkan sebuah bangunan yang diklaim merupakan masjid di Palestina yang dibom saat mengumandangkan azan.

Setelah ditelusuri klaim tersebut menyesatkan. Faktanya video asli telah beredar di YouTube pada 2014, dari judul video tersebut menyebutkan bahwa bangunan tersebut adalah Kuil Uwais al-Qarni yang merupakan bangunan dari makam dari Uwais al-Qarni yang dihancurkan oleh ISIS. Dilansir dari LiputanIslam.com, ISIS telah menghancurkan tempat tersebut sebagai bentuk penegakan tauhid dan menjauhi segala bentuk kesyirikan.

Dengan demikian, video masjid dibom saat mengumandangkan azan di Palestina adalah tidak benar dengan kategori Konten yang Menyesatkan.
"""
# Preprocessing Text Before going to Model Prediction
text_data = text_data.replace('=', '')
text_data = re.sub(r'\[.*?\]|\(.*?\)', '', text_data)
text_data = re.sub(r'\s+', ' ', text_data)

text_sequence = tokenizer.texts_to_sequences([text_data])
text_sequence_pad = pad_sequences(text_sequence, maxlen=max_length, truncating=trunc_type, padding=padding_type)

# Predicting to the Model
predict = model.predict(text_sequence_pad)
print(predict[0])
# Set the threshold to get the label value
threshold = 0.4
predicted_label = 1 if predict[0] > threshold else 0
print("Prediction Label:", predicted_label)