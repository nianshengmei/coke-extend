package pers.warren.coke.maven.plugin.loader.jar;

import pers.warren.coke.maven.plugin.loader.data.RandomAccessData;

/**
 * Callback visitor triggered by {@link CentralDirectoryParser}.
 *
 * @author Phillip Webb
 */
interface CentralDirectoryVisitor {

	void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData);

	void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset);

	void visitEnd();

}
