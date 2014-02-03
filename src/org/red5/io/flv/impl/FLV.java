package org.red5.io.flv.impl;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006-2007 by respective authors (see below). All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later 
 * version. 
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.red5.io.ITag;
import org.red5.io.ITagReader;
import org.red5.io.ITagWriter;
import org.red5.io.flv.IFLV;
import org.red5.io.flv.meta.IMetaData;
import org.red5.io.flv.meta.IMetaService;
import org.red5.server.api.cache.ICacheStore;
import org.red5.server.api.cache.ICacheable;

/**
 * A FLVImpl implements the FLV api
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Dominick Accattato (daccattato@gmail.com)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 * @author Paul Gregoire, (mondain@gmail.com)
 */
public class FLV implements IFLV {

	protected static Log log = LogFactory.getLog(FLV.class.getName());

	private static ICacheStore cache;	
	
	private File file;

	private boolean generateMetadata;

	private IMetaService metaService;

	/**
	 * Default constructor, used by Spring so that parameters 
	 * may be injected.
	 */
	public FLV() {
	}

    /**
     * Create FLV from given file source
     * @param file       File source
     */
    public FLV(File file) {
		this(file, false);
	}

    /**
     * Create FLV from given file source and with specified metadata generation option
     * @param file                    File source
     * @param generateMetadata        Metadata generation option
     */
    public FLV(File file, boolean generateMetadata) {
		this.file = file;
		this.generateMetadata = generateMetadata;
	}

	/**
	 * Sets the cache implementation to be used.
	 * 
	 * @param cache          Cache store
	 */
	public void setCache(ICacheStore cache) {
		FLV.cache = cache;
	}
	
	/** {@inheritDoc}
	 */
	public boolean hasMetaData() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc}
	 */
	public IMetaData getMetaData() throws FileNotFoundException {
		metaService.setInStream(new FileInputStream(file));
		return null;
	}

	/** {@inheritDoc}
	 */
	public boolean hasKeyFrameData() {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc}
	 */
	public void setKeyFrameData(Map keyframedata) {
	}

	/** {@inheritDoc}
	 */
	public Map getKeyFrameData() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc}
	 */
	public void refreshHeaders() throws IOException {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc}
	 */
	public void flushHeaders() throws IOException {
		// TODO Auto-generated method stub

	}

	/** {@inheritDoc}
	 */
	public ITagReader getReader() throws IOException {
		FLVReader reader = null;
		ByteBuffer fileData;
		String fileName = file.getName();

		ICacheable ic = cache.get(fileName);

		// look in the cache before reading the file from the disk
		if (null == ic || (null == ic.getByteBuffer())) {
			if (file.exists()) {
				if (log.isDebugEnabled()) {
					log.debug("File size: " + file.length());
				}
				reader = new FLVReader(file, generateMetadata);
				// get a ref to the mapped byte buffer
				fileData = reader.getFileData();
				// offer the uncached file to the cache
				if (fileData != null && cache.offer(fileName, fileData)) {
					if (log.isDebugEnabled()) {
						log.debug("Item accepted by the cache: " + fileName);
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Item will not be cached: " + fileName);
					}
				}
			} else {
				log.info("Creating new file: " + file);
				file.createNewFile();
			}
		} else {
			fileData = ByteBuffer.wrap(ic.getBytes());
			reader = new FLVReader(fileData, generateMetadata);
		}
		return reader;
	}

	/** {@inheritDoc}
	 */
	public ITagReader readerFromNearestKeyFrame(int seekPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc}
	 */
	public ITagWriter getWriter() throws IOException {
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		ITagWriter writer = new FLVWriter(new FileOutputStream(file));
		writer.writeHeader();
		return writer;
	}

	/** {@inheritDoc} */
    public ITagWriter getAppendWriter() throws IOException {
		// If the file doesnt exist, we cant append to it, so return a writer
		if (!file.exists()) {
			log
					.info("File does not exist, calling writer. This will create a new file.");
			return getWriter();
		}
		ITagReader reader = getReader();
		// Its an empty flv, so no point appending call writer
		if (!reader.hasMoreTags()) {
			reader.close();
			log
					.info("Reader is empty, calling writer. This will create a new file.");
			return getWriter();
		}
		ITag lastTag = null;
		while (reader.hasMoreTags()) {
			lastTag = reader.readTag();
		}
		reader.close();
		FileOutputStream fos = new FileOutputStream(file, true);
        return new FLVWriter(fos, lastTag);
	}

	/** {@inheritDoc}
	 */
	public ITagWriter writerFromNearestKeyFrame(int seekPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
    public void setMetaData(IMetaData meta) throws IOException {
		File tmpFile = new File("test/tmp.flv");
		tmpFile.createNewFile();
		metaService.setInStream(new FileInputStream(file));
		metaService.setOutStream(new FileOutputStream(tmpFile));
		metaService.write(meta);
	}

	/** {@inheritDoc} */
    public void setMetaService(IMetaService service) {
		metaService = service;
	}
}
