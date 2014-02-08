# Overview
'bigdoc' allows you to handle gigabyte order files easily with high performance.
You can search bytes or words / read data/text from huge files.

It is licensed under [MIT license](https://opensource.org/licenses/MIT).

# Quick start
## Search sequence of bytes from a big file quickly.

Search mega-bytes,giga-bytes order file.

```java
package org.example;

import java.io.File;
import java.util.List;

import org.riversun.bigdoc.bin.BigFileSearcher;

public class Example {

	public static void main(String[] args) throws Exception {

		byte[] searchBytes = "hello world.".getBytes("UTF-8");

		File file = new File("/var/tmp/yourBigfile.bin");

		BigFileSearcher searcher = new BigFileSearcher();

		List<Long> findList = searcher.searchBigFile(file, searchBytes);

		System.out.println("positions = " + findList);
	}
}
```
## Performance Test
Search sequence of bytes from big file

### Environment
Tested on AWS t2.*<br>

### Results
<table>
<tr><td>CPU Instance</td> <td>EC2 t2.2xlarge<br>vCPU x 8,32GiB</td>  <td>EC2 t2.xlarge<br>vCPU x 4,16GiB</td><td>EC2 t2.large<br>vCPU x 2,8GiB</td><td>EC2 t2.medium<br>vCPU x 2,4GiB</td>         </tr>
<tr><td>File Size</td>    <td>Time(sec)</td>                              <td>Time(sec)</td>                           <td>Time(sec)</td>                         <td>Time</td>                                    </tr>
<tr><td>10MB</td>         <td>0.5s</td>                              <td>0.6s</td>                           <td>0.8s</td>                         <td>0.8s</td>                                     </tr>
<tr><td>50MB</td>         <td>2.8s</td>                              <td>5.9s</td>                           <td>13.4s</td>                        <td>12.8s</td>                                       </tr>
<tr><td>100MB</td>        <td>5.4s</td>                              <td>10.7s</td>                          <td>25.9s</td>                        <td>25.1s</td>                                        </tr>
<tr><td>250MB</td>        <td>15.7s</td>                             <td>32.6s</td>                          <td>77.1s</td>                        <td>74.8s</td>                                          </tr>
<tr><td>1GB</td>          <td>55.9s</td>                             <td>120.5s</td>                         <td>286.1s</td>                            <td>-</td>                                       </tr>
<tr><td>5GB</td>          <td>259.6s</td>                            <td>566.1s</td>                         <td>-</td>                            <td>-</td>                                         </tr>
<tr><td>10GB</td>         <td>507.0s</td>                            <td>1081.7s</td>                        <td>-</td>                            <td>-</td>                                          </tr>
</table>

Please Note

- Processing speed depends on the number of CPU Cores(included hyper threading) not memory capacity.
- The result is different depending on the environment of the Java ,Java version and compiler or runtime optimization.

# More Details
See javadoc as follows.

https://riversun.github.io/javadoc/bigdoc/

# Downloads
## maven
- You can add dependencies to maven pom.xml file.
```xml
<dependency>
  <groupId>org.riversun</groupId>
  <artifactId>bigdoc</artifactId>
  <version>0.3.0</version>
</dependency>
```
