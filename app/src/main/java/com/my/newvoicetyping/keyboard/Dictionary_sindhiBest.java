package com.my.newvoicetyping.keyboard;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * EN Dictionary_sindhiBest as a simple trie for auto completion.
 */
public class Dictionary_sindhiBest {
	private AutoCompleteTrie_sindhiBest mDictionary = new AutoCompleteTrie_sindhiBest();

	private static Dictionary_sindhiBest instance = null;

	protected Dictionary_sindhiBest(InputStream dictFileStream) {
		new FromFileLoader().execute(dictFileStream);
	}

	public static Dictionary_sindhiBest getInstance(InputStream dictFileStream) {
		if (instance == null) {
			instance = new Dictionary_sindhiBest(dictFileStream);
		}
		return instance;
	}

	/**
	 * Gets the list of words that begin with the specified prefix.
	 * 
	 * @param prefix
	 *            The prefix.
	 * @return A list of words that begin with the specified prefix.
	 */
	public Collection<String> complete(String prefix) {
		return mDictionary.autoComplete(prefix);
	}

	private class FromFileLoader extends
			AsyncTask<InputStream, Void, AutoCompleteTrie_sindhiBest> {
		protected AutoCompleteTrie_sindhiBest doInBackground(
				InputStream... dictFileStreams) {
			try {
				InputStream accStream = dictFileStreams[0];
				BufferedReader accReader = new BufferedReader(
						new InputStreamReader(accStream, "UTF-8"));
				AutoCompleteTrie_sindhiBest dictionary = new AutoCompleteTrie_sindhiBest();
				String line;
				while ((line = accReader.readLine()) != null) {
					dictionary.insert(line.trim());
				}
				return dictionary;
			} catch (Exception ex) {
				LogUtil_sindhiBest.LogError(this.getClass().getName(),
						"Cannot load auto-complete trie for English", ex);
			}

			return new AutoCompleteTrie_sindhiBest();
		}

		protected void onPostExecute(AutoCompleteTrie_sindhiBest dictionary) {
			mDictionary = dictionary;
		}
	}
}
