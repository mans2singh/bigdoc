/*  bigdoc Java lib for easy to read/search from a big document
 *
 *  Copyright (c) 2006-2016 Tom Misawa, riversun.org@gmail.com
 *  
 *  Permission is hereby granted, free of charge, to any person obtaining a
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 *  
 */
package org.riversun.bigdoc;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.riversun.bigdoc.bin.BigFileSearcher;

/**
 * test for BigFileSearcher
 *@author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class TestBigFileSearcher extends TestBase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_search_defaultBuffSize() {

		final String testText = "rejoice";
		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		final List<Long> result = obj.search(file, searchBytes);

		assertThat(result, contains(15667L, 164920L));
	}

	/**
	 * check whether method works correctly even if the buffer size is changed
	 */
	@Test
	public void test_search_with_variousBuffSize() {

		final String testText = "rejoice";

		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		final List<Long> result = obj.search(file, searchBytes);

		int[] bufSizes = {
				testText.length(),
				100,
				// prime numbers
				101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157,
				// kilo bytes
				1024, 1024 * 2, 1024 * 4,
				// same of target file
				(int) file.length(),
				// around of target file size
				(int) file.length() - 1, (int) file.length() + 1,
				// mega bytes
				1024 * 1024, 2 * 1024 * 1024, 4 * 1024 * 1024
		};

		for (int bufSize : bufSizes) {
			obj.setBufferSize(bufSize);
			assertThat(result, contains(15667L, 164920L));
		}

	}

	@Test
	public void test_searchPartially_from_firstPosOfFile() {
		final String testText = "rejoice";

		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		final long startPos = 0;

		// check if contains (readSize is just)
		{
			final long readSize = 15667L + testText.length();

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			assertThat(result, (contains(15667L)));
		}

		// check if contains (readSize is enough)
		{
			final long readSize = 15667L + testText.length() + 100;

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			assertThat(result, (contains(15667L)));
		}

		// check if "NOT" contains(readSize is not enough)
		{
			// "-1" makes readSize not enough;
			final long readSize = 15667L + testText.length() - 1;

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			assertThat(result, not(contains(15667L)));
		}

	}

	@Test
	public void test_searchPartially_from_midPosOfFile_with_enough_readSize() {
		final String testText = "rejoice";

		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		// check if contains (target bytes stars with startPos)
		{

			// 15667L means where target bytes stars with.
			final long startPos = 15667L;

			// means enough size to read
			final long readSize = file.length();

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			assertThat(result, (contains(15667L, 164920L)));
		}

		// check if "NOT" contains
		{

			// 15667L+1 means after target bytes of the first "rejoice" in the
			// file.
			final long startPos = 15667L + 1;

			// means enough size to read
			final long readSize = file.length();

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			assertThat(result, (contains(164920L)));
		}

	}

	@Test
	public void test_searchPartially_from_midPosOfFile_with_not_enough_readSize() {
		final String testText = "rejoice";

		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		// check if "NOT" contains
		{

			// 15667L+1 means after target bytes of the first "rejoice" in the
			// file.
			final long startPos = 15667L + 1;

			// means not enough size to read
			final long readSize = 1000;

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			// nothing is discovered.
			assertEquals(result.size(), 0);

		}

	}

	@Test
	public void test_searchPartially_from_midPosOfFile_with_readSiZe_with_variousBufferSize() {
		final String testText = "rejoice";

		final byte[] searchBytes = getFromUTF8(testText);

		final BigFileSearcher obj = new BigFileSearcher();

		final File file = getFileFromResource("Coriolanus.txt");

		// the second "rejoice" is placed in second half of the file
		{

			final long startPos = file.length() / 2;

			final long readSize = file.length() / 2;

			final List<Long> result = obj.searchPartially(file, searchBytes, startPos, readSize);

			int[] bufSizes = {
					testText.length(),
					100,
					// prime numbers
					101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157,
					// kilo bytes
					1024, 1024 * 2, 1024 * 4,
					// same of readSize
					(int) readSize,
					// same of target file
					(int) file.length(),
					// around of target file size
					(int) file.length() - 1, (int) file.length() + 1,
					// mega bytes
					1024 * 1024, 2 * 1024 * 1024, 4 * 1024 * 1024
			};

			for (int bufSize : bufSizes) {
				obj.setBufferSize(bufSize);
				assertThat(result, (contains(164920L)));

			}
		}

	}

}