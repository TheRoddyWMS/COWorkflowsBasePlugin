/*
 * Copyright (c) 2018 German Cancer Research Center (DKFZ).
 *
 * Distributed under the MIT License (license terms are at https://www.github.com/eilslabs/Roddy/LICENSE.txt).
 */
package de.dkfz.b080.co.knowledge.metadata

import spock.lang.Specification

class COMetadataAccessorSpec extends Specification {

    def "GrepPathElementFromFilenames"() {

        when:
        def files = [new File("/a/b//c1/sampleName1/bla/bla/bla"),
                     new File("/a/b/c2/sampleName1/bla/bla/blub"),
                     new File("/a/b/c2/sampleName2/bla/blub/")]
        def pattern = "/a/b/c/\${sample}"

        then:
        assert COMetadataAccessor.grepPathElementFromFilenames(
                pattern,
                '${sample}',
                files).collect { it.x }.equals(["sampleName1", "sampleName1", "sampleName2"])
        assert COMetadataAccessor.grepPathElementFromFilenames(
                pattern,
                '${sample}',
                files ).collect { it.y }.equals(["/a/b/c1/sampleName1", "/a/b/c2/sampleName1", "/a/b/c2/sampleName2"].collect { new File(it) })
    }

    def "GrepPathElementFromFilenames_tooShortPath"() {

        when:
        def file = new File("/a/b/c/")
        def pattern = "/a/b/c/\${sample}"

        then:
        try {
            COMetadataAccessor.grepPathElementFromFilenames(pattern, '${sample}', [file] as List<File>)
        } catch (RuntimeException e) {
            assert e.message.startsWith("Path to file")
        }
    }

}